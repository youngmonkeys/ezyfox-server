package com.tvd12.ezyfoxserver.support.test.controller.app;

import com.tvd12.ezyfox.annotation.EzyFeature;
import com.tvd12.ezyfox.annotation.EzyManagement;
import com.tvd12.ezyfox.annotation.EzyPayment;
import com.tvd12.ezyfox.core.annotation.EzyDoHandle;
import com.tvd12.ezyfox.core.annotation.EzyRequestController;

@EzyManagement
@EzyPayment
@EzyRequestController("v1.2.2")
public class V122FeatureController {

    @EzyManagement
    @EzyPayment
    @EzyFeature("hello.world")
    @EzyDoHandle("hello")
    public void hello() {}
    
    @EzyFeature("")
    @EzyDoHandle("hello2")
    public void hello2() {}
}
