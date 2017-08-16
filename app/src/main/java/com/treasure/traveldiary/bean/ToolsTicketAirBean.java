package com.treasure.traveldiary.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by treasure on 2017.05.17.
 */

public class ToolsTicketAirBean implements Serializable {

    /**
     * msg : success
     * result : [{"airLines":"中国东方航空公司","flightNo":"MU4011","flightRate":"97%","flightTime":"2h28m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"10:20","planTime":"07:15","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"深圳航空公司","flightNo":"ZH5069","flightRate":"97%","flightTime":"2h28m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"10:20","planTime":"07:15","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"中国东方航空公司","flightNo":"MU9529","flightRate":"80%","flightTime":"4h1m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T1","planArriveTime":"13:45","planTime":"09:25","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"上海航空公司","flightNo":"FM9269","flightRate":"95%","flightTime":"3h40m","from":"虹桥国际机场","fromAirportCode":"SHA","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"13:40","planTime":"09:25","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"厦门航空公司","flightNo":"MF1350","flightRate":"75%","flightTime":"2h27m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"15:15","planTime":"12:20","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"上海航空公司","flightNo":"FM9529","flightRate":"80%","flightTime":"4h1m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T1","planArriveTime":"13:45","planTime":"09:25","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"上海航空公司","flightNo":"FM9251","flightRate":"82%","flightTime":"2h28m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T1","planArriveTime":"16:35","planTime":"13:35","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"中国国际航空公司","flightNo":"CA1973","flightRate":"91%","flightTime":"2h28m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"18:05","planTime":"15:00","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"海南航空公司","flightNo":"HU7320","flightRate":"75%","flightTime":"2h30m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"23:50","planTime":"20:45","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"上海吉祥航空公司","flightNo":"HO1271","flightRate":"97%","flightTime":"2h28m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"10:20","planTime":"07:15","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"中国东方航空公司","flightNo":"MU9269","flightRate":"95%","flightTime":"3h40m","from":"虹桥国际机场","fromAirportCode":"SHA","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"13:40","planTime":"09:25","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"中国东方航空公司","flightNo":"MU9251","flightRate":"82%","flightTime":"2h28m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T1","planArriveTime":"16:35","planTime":"13:35","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"中国南方航空公司","flightNo":"CZ3516","flightRate":"75%","flightTime":"2h27m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"15:15","planTime":"12:20","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"深圳航空公司","flightNo":"ZH1973","flightRate":"91%","flightTime":"2h28m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"18:05","planTime":"15:00","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"中国东方航空公司","flightNo":"MU9531","flightRate":"80%","flightTime":"2h43m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T1","planArriveTime":"19:30","planTime":"16:30","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"上海航空公司","flightNo":"FM9531","flightRate":"80%","flightTime":"2h43m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T1","planArriveTime":"19:30","planTime":"16:30","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"中国南方航空公司","flightNo":"CZ6766","flightRate":"68%","flightTime":"2h23m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"01:05","planTime":"22:10","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,三,五,六,日"},{"airLines":"山东航空公司","flightNo":"SC1973","flightRate":"91%","flightTime":"2h28m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"18:05","planTime":"15:00","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"中国东方航空公司","flightNo":"MU4476","flightRate":"68%","flightTime":"2h23m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"01:05","planTime":"22:10","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,三,五,六,日"},{"airLines":"中国东方航空公司","flightNo":"MU3257","flightRate":"75%","flightTime":"2h27m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"15:15","planTime":"12:20","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,二,三,四,五,六,日"},{"airLines":"厦门航空公司","flightNo":"MF4310","flightRate":"68%","flightTime":"2h23m","from":"浦东国际机场","fromAirportCode":"PVG","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"01:05","planTime":"22:10","to":"美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"一,三,五,六,日"},{"airLines":"天津航空","flightNo":"GS6588","flightRate":"86%","from":"上海浦东国际机场","fromAirportCode":"SHA","fromCityCode":"SHA","fromCityName":"上海","fromTerminal":"T2","planArriveTime":"17:40","planTime":"13:15","to":"海口美兰国际机场","toAirportCode":"HAK","toCityCode":"HAK","toCityName":"海口","toTerminal":"","week":"二,四,六"}]
     * retCode : 200
     */

    private String msg;
    private String retCode;
    private List<ResultBean> result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * airLines : 中国东方航空公司
         * flightNo : MU4011
         * flightRate : 97%
         * flightTime : 2h28m
         * from : 浦东国际机场
         * fromAirportCode : PVG
         * fromCityCode : SHA
         * fromCityName : 上海
         * fromTerminal : T2
         * planArriveTime : 10:20
         * planTime : 07:15
         * to : 美兰国际机场
         * toAirportCode : HAK
         * toCityCode : HAK
         * toCityName : 海口
         * toTerminal :
         * week : 一,二,三,四,五,六,日
         */

        private String airLines;
        private String flightNo;
        private String flightRate;
        private String flightTime;
        private String from;
        private String fromAirportCode;
        private String fromCityCode;
        private String fromCityName;
        private String fromTerminal;
        private String planArriveTime;
        private String planTime;
        private String to;
        private String toAirportCode;
        private String toCityCode;
        private String toCityName;
        private String toTerminal;
        private String week;

        public String getAirLines() {
            return airLines;
        }

        public void setAirLines(String airLines) {
            this.airLines = airLines;
        }

        public String getFlightNo() {
            return flightNo;
        }

        public void setFlightNo(String flightNo) {
            this.flightNo = flightNo;
        }

        public String getFlightRate() {
            return flightRate;
        }

        public void setFlightRate(String flightRate) {
            this.flightRate = flightRate;
        }

        public String getFlightTime() {
            return flightTime;
        }

        public void setFlightTime(String flightTime) {
            this.flightTime = flightTime;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getFromAirportCode() {
            return fromAirportCode;
        }

        public void setFromAirportCode(String fromAirportCode) {
            this.fromAirportCode = fromAirportCode;
        }

        public String getFromCityCode() {
            return fromCityCode;
        }

        public void setFromCityCode(String fromCityCode) {
            this.fromCityCode = fromCityCode;
        }

        public String getFromCityName() {
            return fromCityName;
        }

        public void setFromCityName(String fromCityName) {
            this.fromCityName = fromCityName;
        }

        public String getFromTerminal() {
            return fromTerminal;
        }

        public void setFromTerminal(String fromTerminal) {
            this.fromTerminal = fromTerminal;
        }

        public String getPlanArriveTime() {
            return planArriveTime;
        }

        public void setPlanArriveTime(String planArriveTime) {
            this.planArriveTime = planArriveTime;
        }

        public String getPlanTime() {
            return planTime;
        }

        public void setPlanTime(String planTime) {
            this.planTime = planTime;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getToAirportCode() {
            return toAirportCode;
        }

        public void setToAirportCode(String toAirportCode) {
            this.toAirportCode = toAirportCode;
        }

        public String getToCityCode() {
            return toCityCode;
        }

        public void setToCityCode(String toCityCode) {
            this.toCityCode = toCityCode;
        }

        public String getToCityName() {
            return toCityName;
        }

        public void setToCityName(String toCityName) {
            this.toCityName = toCityName;
        }

        public String getToTerminal() {
            return toTerminal;
        }

        public void setToTerminal(String toTerminal) {
            this.toTerminal = toTerminal;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }
    }
}
