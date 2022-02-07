package com.dayrain.net.tools.port;

import java.util.Objects;

public class PortInfo {
    private int port;
    private int pid;
    private String processName;
    private String protocol;

    public PortInfo(int port, int pid, String protocol) {
        this.port = port;
        this.pid = pid;
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortInfo portInfo = (PortInfo) o;
        return port == portInfo.port && pid == portInfo.pid && Objects.equals(processName, portInfo.processName) && Objects.equals(protocol, portInfo.protocol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(port, pid, processName, protocol);
    }

    @Override
    public String toString() {
        return "PortInfo{" +
                "port=" + port +
                ", pid=" + pid +
                ", processName='" + processName + '\'' +
                ", protocol='" + protocol + '\'' +
                '}';
    }
}
