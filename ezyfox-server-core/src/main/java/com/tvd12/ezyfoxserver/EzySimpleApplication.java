package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import lombok.Getter;
import lombok.Setter;

import static com.tvd12.ezyfox.util.EzyProcessor.processWithLogException;

@Setter
@Getter
public class EzySimpleApplication
    extends EzyChildComponent
    implements EzyApplication, EzyDestroyable {

    protected EzyAppSetting setting;
    protected EzyAppUserManager userManager;
    protected EzyAppRequestController requestController;

    public EzySimpleApplication() {
        this.requestController = EzyAppRequestController.DEFAULT;
    }

    @Override
    public boolean equals(Object obj) {
        return new EzyEquals<EzySimpleApplication>()
            .function(t -> t.setting)
            .isEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return new EzyHashCodes()
            .append(setting)
            .toHashCode();
    }

    @Override
    public void destroy() {
        super.destroy();
        if (userManager != null) {
            processWithLogException(() -> userManager.destroy());
        }
        this.setting = null;
        this.userManager = null;
        this.requestController = null;
    }

    @Override
    public String toString() {
        return setting.getName() + "(id = " + setting.getId() + ")";
    }
}
