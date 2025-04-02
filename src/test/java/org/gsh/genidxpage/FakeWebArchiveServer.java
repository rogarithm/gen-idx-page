package org.gsh.genidxpage;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

class FakeWebArchiveServer {
    WireMockServer instance;

    public FakeWebArchiveServer() {
        this.instance = new WireMockServer(wireMockConfig().port(8080));
    }

    public void start() {
        this.instance.start();
    }

    public void stop() {
        this.instance.stop();
    }
}
