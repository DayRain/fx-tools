package com.dayrain.net.tools;

import com.dayrain.net.tools.port.PortInfo;

import java.util.List;

public interface PortCheckService {
    List<PortInfo> listPort();
    PortInfo getByPort(int port);
    void kill(int pid);
}
