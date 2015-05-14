package com.superscores.android.compat;

import android.annotation.TargetApi;
import android.os.Build;
import android.widget.RelativeLayout;

@TargetApi(17)
public class RelativeLayoutCompat {

    public static final int START_OF;
    public static final int END_OF;
    public static final int ALIGN_START;
    public static final int ALIGN_END;
    public static final int ALIGN_PARENT_START;
    public static final int ALIGN_PARENT_END;

    static {
        if (Build.VERSION.SDK_INT >= 17) {
            START_OF = RelativeLayout.START_OF;
            END_OF = RelativeLayout.END_OF;
            ALIGN_START = RelativeLayout.ALIGN_START;
            ALIGN_END = RelativeLayout.ALIGN_END;
            ALIGN_PARENT_START = RelativeLayout.ALIGN_PARENT_START;
            ALIGN_PARENT_END = RelativeLayout.ALIGN_PARENT_END;
        } else {
            START_OF = RelativeLayout.LEFT_OF;
            END_OF = RelativeLayout.RIGHT_OF;
            ALIGN_START = RelativeLayout.ALIGN_LEFT;
            ALIGN_END = RelativeLayout.ALIGN_RIGHT;
            ALIGN_PARENT_START = RelativeLayout.ALIGN_PARENT_LEFT;
            ALIGN_PARENT_END = RelativeLayout.ALIGN_PARENT_RIGHT;
        }
    }

}