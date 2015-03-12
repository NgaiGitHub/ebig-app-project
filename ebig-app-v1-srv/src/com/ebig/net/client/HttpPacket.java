package com.ebig.net.client;

import java.util.*;

public class HttpPacket {
    private String sessionid;//SESSION
    private String srvtype; //报文类型，在配置文件中定义报文的类型和处理类
    private List inparams;  //输入参数
    private Object   outresult; //输出结果OBJECT    
    public String baservleturl;
    
    public String procclass;
    public String method;    
    public String module_id;
    
    public HttpPacket() {
        sessionid = "";
        srvtype = "";
        inparams = new ArrayList();
        outresult = new Object();
    }


    public List getInparams() {
        return inparams;
    }

    public Object getOutresult() {
        return outresult;
    }

    public String getSrvtype() {
        return srvtype;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setInparams(List inparams) {
        this.inparams = inparams;
    }

    public void setOutresult(Object outresult) {
        this.outresult = outresult;
    }

    public void setSrvtype(String srvtype) {
        this.srvtype = srvtype;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public void setParameter(Object value){
        inparams.add(value);
    }
    
    public Object getResults(){
        return outresult;
    }

	public String getModule_id() {
		return module_id;
	}

	public void setModule_id(String module_id) {
		this.module_id = module_id;
	}

	public String getProcclass() {
		return procclass;
	}

	public void setProcclass(String procclass) {
		this.procclass = procclass;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getBaservleturl() {
		return baservleturl;
	}

	public void setBaservleturl(String baservleturl) {
		this.baservleturl = baservleturl;
	}
    
    
}
