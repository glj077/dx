package com.example.dx_4g.funclass;

import java.util.List;

public class DX_4G_Reg {

    /**
     * count : 17
     * data : [{"addr":2048,"value":26214,"time":"2020-05-19 15:07:04.587317","name":"液位","template":"return val;","history":1},{"addr":2049,"value":16908,"time":"2020-05-19 15:07:04.587317","name":null,"template":null,"history":1},{"addr":2050,"value":0,"time":"2020-05-19 15:06:54.406869","name":"一号泵运行状态","template":"return val;","history":1},{"addr":2051,"value":0,"time":"2020-05-19 15:06:33.916389","name":"二号泵运行状态","template":"return val;","history":1},{"addr":2052,"value":1,"time":"2020-05-19 15:06:44.166325","name":"三号泵运行状态","template":"return val;","history":1},{"addr":2053,"value":0,"time":"2020-05-19 15:06:33.916389","name":"四号泵运行状态","template":"return val;","history":1},{"addr":2054,"value":1,"time":"2020-05-19 15:06:54.406869","name":"液位高报警","template":"return val;","history":1},{"addr":2055,"value":0,"time":"2020-05-19 15:06:44.166325","name":"液位低报警","template":"return val;","history":1},{"addr":2056,"value":0,"time":"2020-05-19 15:06:46.910997","name":"一号泵启停控制","template":"return val;","history":0},{"addr":2057,"value":0,"time":"2020-05-18 11:13:13.385818","name":"二号泵启停控制","template":"return val;","history":0},{"addr":2058,"value":1,"time":"2020-05-19 15:06:36.389141","name":"三号泵启停控制","template":"return val;","history":0},{"addr":2059,"value":0,"time":"2020-05-18 11:13:13.385818","name":"四号泵启停控制","template":"return val;","history":0},{"addr":2060,"value":0,"time":"2020-05-19 15:07:08.18785","name":"数据采集开关","template":"return val;","history":0},{"addr":2061,"value":0,"time":"2020-05-18 11:13:13.385818","name":"液位上限设定值","template":"return val;","history":0},{"addr":2062,"value":0,"time":"2020-05-18 11:13:13.385818","name":null,"template":null,"history":0},{"addr":2063,"value":0,"time":"2020-05-18 11:13:13.385818","name":"液位下限设定值","template":"return val;","history":0},{"addr":2064,"value":0,"time":"2020-05-18 11:13:13.385818","name":null,"template":null,"history":0}]
     */

    private int count;
    private List<DataBean> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * addr : 2048
         * value : 26214
         * time : 2020-05-19 15:07:04.587317
         * name : 液位
         * template : return val;
         * history : 1
         */

        private int addr;
        private int value;
        private String time;
        private String name;
        private String template;
        private int history;

        public int getAddr() {
            return addr;
        }

        public void setAddr(int addr) {
            this.addr = addr;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTemplate() {
            return template;
        }

        public void setTemplate(String template) {
            this.template = template;
        }

        public int getHistory() {
            return history;
        }

        public void setHistory(int history) {
            this.history = history;
        }
    }
}
