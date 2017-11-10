package com.tvd12.ezyfoxserver.ssl;

import com.tvd12.properties.file.annotation.Property;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimpleSslConfig implements EzySslConfig {

    @Property("ssl.keystore")
    protected String keyStoreFile = "ssl/ssl-keystore.txt";
    
    @Property("ssl.keystore_password")
    protected String keyStorePasswordFile = "ssl/ssl-keystore-password.txt";
    
    @Property("ssl.certificate_password")
    protected String certificatePasswordFile = "ssl/ssl-certificate-password.txt";
    
}
