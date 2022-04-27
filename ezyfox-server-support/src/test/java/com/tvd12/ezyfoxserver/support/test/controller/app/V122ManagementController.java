package com.tvd12.ezyfoxserver.support.test.controller.app;

import com.tvd12.ezyfox.annotation.EzyFeature;
import com.tvd12.ezyfox.annotation.EzyManagement;
import com.tvd12.ezyfox.annotation.EzyPayment;
import com.tvd12.ezyfox.core.annotation.EzyDoHandle;
import com.tvd12.ezyfox.core.annotation.EzyRequestController;

@EzyFeature("hello.world")
@EzyRequestController("v1.2.2/feature")
public class V122ManagementController {

    @EzyManagement
    @EzyPayment
    @EzyFeature("hello.world")
    @EzyDoHandle("hello")
    public void hello() {}

    @EzyManagement
    @EzyPayment
    @EzyFeature("")
    @EzyDoHandle("hello2")
    public void hello2() {}
}
