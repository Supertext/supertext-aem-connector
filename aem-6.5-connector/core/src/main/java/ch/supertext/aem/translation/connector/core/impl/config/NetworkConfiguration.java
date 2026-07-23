package ch.supertext.aem.translation.connector.core.impl.config;

import java.util.Map;

import ch.supertext.aem.translation.connector.core.ProxyConfiguration;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Component(label="Supertext Translation Connector Network", description="Network configuration for the Supertext Connector", metatype=true)
public class NetworkConfiguration
        implements ProxyConfiguration
{
    private static final Logger log = LoggerFactory.getLogger(NetworkConfiguration.class);

    @Property(label="Enable Proxy", description="Whether to enable or disable this particular proxy configuration. The default value is false.", boolValue={false})
    private static final String PROXY_ENABLED = "proxy.enabled";
    @Property(label="Proxy Host", description="Host name (or IP Address) of the HTTP Proxy. This property is ignored if this proxy configuration is disabled. This property does not have a default value. ")
    private static final String PROXY_HOST = "proxy.host";
    @Property(label="Proxy Port", description="TCP port of the HTTP Proxy. This property is ignored if this proxy configuration is disabled. This property does not have a default value")
    private static final String PROXY_PORT = "proxy.port";
    @Property(label="Proxy User", description="The name of the user to authenticate as with the HTTP Proxy Host. If this field is empty, the proxy is considered to not be authenticated. The default is empty.")
    private static final String PROXY_USER = "proxy.user";
    @Property(label="Proxy Password", description="The password of the HTTP Proxy user to authenticate with. The default is empty.")
    private static final String PROXY_PASSWORD = "proxy.password";

    private boolean proxyEnabled;
    private String proxyHost;
    private int proxyPort;
    private String proxyUser;
    private String proxyPassword;

    @Activate
    protected void activate(Map<String, Object> properties)
    {
        this.proxyHost = PropertiesUtil.toString(properties.get(PROXY_HOST), null);
        this.proxyPort = PropertiesUtil.toInteger(properties.get(PROXY_PORT), 0);
        this.proxyUser = PropertiesUtil.toString(properties.get(PROXY_USER), null);
        this.proxyPassword = PropertiesUtil.toString(properties.get(PROXY_PASSWORD), null);
        this.proxyEnabled = ((PropertiesUtil.toBoolean(properties.get(PROXY_ENABLED), false)) && (this.proxyHost != null) && (this.proxyPort != 0));

        log.debug("ProxyConfiguration.proxyEnabled {}", this.proxyEnabled ? "enabled" : "disabled");
        log.debug("ProxyConfiguration.proxyHost {}", this.proxyHost == null ? "" : this.proxyHost);
        log.debug("ProxyConfiguration.proxyPort {}", this.proxyPort);
    }

    public boolean isProxyEnabled()
    {
        return this.proxyEnabled;
    }

    public boolean isAuthenticationNeeded()
    {
        return this.proxyPassword != null && this.proxyUser != null;
    }

    public String getProxyHost()
    {
        return this.proxyHost;
    }

    public int getProxyPort()
    {
        return this.proxyPort;
    }

    public String getProxyUser()
    {
        return this.proxyUser;
    }

    public String getProxyPassword()
    {
        return this.proxyPassword;
    }
}
