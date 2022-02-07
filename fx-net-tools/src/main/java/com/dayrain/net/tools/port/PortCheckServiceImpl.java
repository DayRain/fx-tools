package com.dayrain.net.tools.port;

import com.dayrain.net.tools.CmdUtils;
import com.dayrain.net.tools.ListUtils;
import com.dayrain.net.tools.PortCheckService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PortCheckServiceImpl implements PortCheckService {

    @Override
    public List<PortInfo> listPort() {
        List<PortInfo> portInfos = analyse(CmdConstants.LIST_PORT);
        Map<Integer, String> map = getProcessMap();
        if(!ListUtils.isEmpty(portInfos)) {
            for (PortInfo portInfo : portInfos) {
                if(map.containsKey(portInfo.getPid())) {
                    portInfo.setProcessName(map.get(portInfo.getPid()));
                }
            }
        }

        //去重
        portInfos = portInfos.stream().distinct().collect(Collectors.toList());
        return portInfos;
    }

    private Map<Integer, String> getProcessMap() {
        List<String> tasklist = CmdUtils.execute("tasklist");

        Map<Integer, String> map = new HashMap<>();
        if(!ListUtils.isEmpty(tasklist) && tasklist.size() > 2) {
            String[] names = tasklist.get(1).split("\\s+");
            int nameLen = names[0].length();
            int pidLen = names[1].length();

            for (int i = 2; i < tasklist.size(); i++) {
                String pInfo = tasklist.get(i);
                int pid = Integer.parseInt(pInfo.substring(nameLen, nameLen + pidLen + 1).trim());
                String name = pInfo.substring(0, nameLen).trim();
                map.put(pid, name);
            }
        }
        return map;
    }

    @Override
    public PortInfo getByPort(int port) {
        List<PortInfo> list = listPort();
        if(list.isEmpty()) {
            return null;
        }

        for (PortInfo portInfo : list) {
            if(port == portInfo.getPort()) {
                return portInfo;
            }
        }
        return null;
    }

    @Override
    public void kill(int pid) {
        CmdUtils.execute(CmdConstants.KILL_PROCESS + pid);
    }

    private List<PortInfo> analyse(String cmd) {
        List<PortInfo> list = new ArrayList<>();
        List<String> cmdDetails = CmdUtils.execute(cmd);
        for (String detail : cmdDetails) {
            if(detail.startsWith("TCP")) {
                String[] cmds = detail.split("\\s+");
                PortInfo portInfo = new PortInfo(getPort(cmds[1]),Integer.parseInt(cmds[cmds.length - 1]), "TCP");
                list.add(portInfo);
            }
        }
        return list;
    }

    private int getPort(String cmd) {
        String[] splits = cmd.split(":");
        return Integer.parseInt(splits[splits.length - 1]);
    }

    public static void main(String[] args) {
        PortCheckServiceImpl portCheckService = new PortCheckServiceImpl();
        List<PortInfo> portInfos = portCheckService.listPort();
        System.out.println(portInfos);
    }
}
