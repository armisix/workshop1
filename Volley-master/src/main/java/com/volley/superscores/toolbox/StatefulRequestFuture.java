/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.volley.superscores.toolbox;

import android.support.annotation.NonNull;

import com.volley.Request;
import com.volley.superscores.NetworkResponseWrapper;
import com.volley.superscores.RequestResult;
import com.volley.superscores.Response;
import com.volley.superscores.SuperScoresRequest;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A Future that represents a Volley request.
 *
 * Used by providing as your response and error listeners. For example:
 *
 * <pre>
 * RequestFuture&lt;JSONObject&gt; future = RequestFuture.newFuture();
 * MyRequest request = new MyRequest(URL, future, future);
 *
 * // If you want to be able to cancel the request:
 * future.setRequest(requestQueue.add(request));
 *
 * // Otherwise:
 * requestQueue.add(request);
 *
 * try {
 *     JSONObject response = future.get();
 *     // do something with response
 * } catch (InterruptedException e) {
 *     // handle the error
 * } catch (ExecutionException e) {
 *     // handle the error
 * }
 * </pre>
 *
 * @param <D> <E> The type of parsed response this future expects.
 */
public class StatefulRequestFuture<D, E> implements Future<RequestResult<D, E>>,
        Response.Listener<D>, Response.ErrorListener<E> {

    private Request<?> mRequest;
    private boolean mResultReceived = false;
    private RequestResult<D, E> mResult;

    public static <D, E> StatefulRequestFuture<D, E> newFuture() {
        return new StatefulRequestFuture<>();
    }

    public void setRequest(SuperScoresRequest<D, E> request) {
        mRequest = request;
    }

    @Override
    public synchronized boolean cancel(boolean mayInterruptIfRunning) {
        if (mRequest == null) {
            return false;
        }

        if (!isDone()) {
            mRequest.cancel();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public RequestResult<D, E> get() throws InterruptedException {
        try {
            return doGet(null);
        } catch (TimeoutException e) {
            throw new AssertionError(e);
        }
    }

    @Override
    public RequestResult<D, E> get(long timeout,
            @NonNull TimeUnit unit) throws InterruptedException, TimeoutException {
        return doGet(TimeUnit.MILLISECONDS.convert(timeout, unit));
    }

    private synchronized RequestResult<D, E> doGet(
            Long timeoutMs) throws InterruptedException, TimeoutException {

        if (mResultReceived) {
            return mResult;
        }

        if (timeoutMs == null) {
            this.wait(0);
        } else if (timeoutMs > 0) {
            this.wait(timeoutMs);
        }

        if (!mResultReceived) {
            throw new TimeoutException();
        }

        return mResult;
    }

    @Override
    public boolean isCancelled() {
        return mRequest != null && mRequest.isCanceled();
    }

    @Override
    public synchronized boolean isDone() {
        return mResultReceived || isCancelled();
    }

    @Override
    public synchronized void onResponse(NetworkResponseWrapper<D> response) {
        mResultReceived = true;
        mResult = RequestResult.success(response);
        this.notifyAll();
    }

    @Override
    public synchronized void onErrorResponse(NetworkResponseWrapper<E> error) {
        mResultReceived = true;
        mResult = RequestResult.error(error);
        this.notifyAll();
    }
}