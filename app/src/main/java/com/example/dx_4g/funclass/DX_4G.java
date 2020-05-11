//定义云端数据JSON格式

package com.example.dx_4g.funclass;

import java.util.List;

public class DX_4G {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 26923
         * name : DX2100_F101
         * sn : DXR02010F270084
         * tunnel_id : 1579
         * storage_state : 1
         * traffic_state : 1
         * online : 0
         * ip : 192.168.200.166
         */

        private int id;
        private String name;
        private String sn;
        private int tunnel_id;
        private int storage_state;
        private int traffic_state;
        private int online;
        private String ip;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public int getTunnel_id() {
            return tunnel_id;
        }

        public void setTunnel_id(int tunnel_id) {
            this.tunnel_id = tunnel_id;
        }

        public int getStorage_state() {
            return storage_state;
        }

        public void setStorage_state(int storage_state) {
            this.storage_state = storage_state;
        }

        public int getTraffic_state() {
            return traffic_state;
        }

        public void setTraffic_state(int traffic_state) {
            this.traffic_state = traffic_state;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }
}
