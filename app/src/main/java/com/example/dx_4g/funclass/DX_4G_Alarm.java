package com.example.dx_4g.funclass;

import java.util.List;

public class DX_4G_Alarm {

    /**
     * paging : {"page":1,"limit":20,"total":"83","pageCount":5,"offset":0}
     * data : [{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/05/27 09:29:55 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-05-27 09:30:03.893547"},{"device_id":"35717","name":"WaterHighLimit","title":"液位高","content":"2020/05/19 15:06:54   液位高","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-05-19 15:06:58.783007"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/05/18 11:13:05 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-05-18 11:13:12.935528"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/05/18 09:36:38 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-05-18 09:36:44.330961"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/05/11 16:55:34 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-05-11 16:55:35.482394"},{"device_id":"35717","name":"WaterHighLimit","title":"液位高","content":"2020/05/11 16:34:56   液位高","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-05-11 16:34:57.543978"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/05/11 16:05:49 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-05-11 16:06:43.86418"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/04/24 09:54:26 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-04-24 09:54:43.814972"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/04/22 15:21:39 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-04-22 15:21:43.890363"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/04/21 10:47:06 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-04-21 10:47:08.784937"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/04/21 09:37:35 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-04-21 09:37:38.136404"},{"device_id":"35717","name":"WaterHighLimit","title":"液位高","content":"2020/04/21 09:13:03   液位高","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-04-21 09:13:06.538798"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/04/21 08:59:50 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-04-21 09:00:00.086415"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/04/20 09:27:02 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-04-20 09:27:04.29669"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/04/20 09:00:29 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-04-20 09:00:34.216638"},{"device_id":"35717","name":"WaterHighLimit","title":"液位高","content":"2020/04/20 08:46:27   液位高","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-04-20 08:46:28.962839"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/04/20 08:01:09 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-04-20 08:01:16.330316"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/04/17 15:50:43 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-04-17 15:50:45.678028"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/04/17 14:25:49 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-04-17 14:25:51.788366"},{"device_id":"35717","name":"WaterLowLimt","title":"液位低","content":"2020/04/17 11:56:45 液位低","sms":null,"email":{"result":0,"addr":"TO:{glj077@qq.com;}"},"created":"2020-04-17 11:56:50.142602"}]
     */

    private PagingBean paging;
    private List<DataBean> data;

    public PagingBean getPaging() {
        return paging;
    }

    public void setPaging(PagingBean paging) {
        this.paging = paging;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class PagingBean {
        /**
         * page : 1
         * limit : 20
         * total : 83
         * pageCount : 5
         * offset : 0
         */

        private int page;
        private int limit;
        private String total;
        private int pageCount;
        private int offset;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }
    }

    public static class DataBean {
        /**
         * device_id : 35717
         * name : WaterLowLimt
         * title : 液位低
         * content : 2020/05/27 09:29:55 液位低
         * sms : null
         * email : {"result":0,"addr":"TO:{glj077@qq.com;}"}
         * created : 2020-05-27 09:30:03.893547
         */

        private String device_id;
        private String name;
        private String title;
        private String content;
        private Object sms;
        private EmailBean email;
        private String created;

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Object getSms() {
            return sms;
        }

        public void setSms(Object sms) {
            this.sms = sms;
        }

        public EmailBean getEmail() {
            return email;
        }

        public void setEmail(EmailBean email) {
            this.email = email;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public static class EmailBean {
            /**
             * result : 0
             * addr : TO:{glj077@qq.com;}
             */

            private int result;
            private String addr;

            public int getResult() {
                return result;
            }

            public void setResult(int result) {
                this.result = result;
            }

            public String getAddr() {
                return addr;
            }

            public void setAddr(String addr) {
                this.addr = addr;
            }
        }
    }
}
