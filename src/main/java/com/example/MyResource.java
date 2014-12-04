package com.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.spy.memcached.MemcachedClient;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.*;
import java.util.logging.Logger;

/**
 * Root resource (exposed at "myresource" path)
 * http://stackoverflow.com/questions/3496209/input-and-output-binary-streams-using-jersey/12573173#12573173
 */
@Path("test")
public class MyResource {
    private static final Logger logger = Logger.getLogger("MyResource");




    @POST
    @Path("action")
    public String action(@FormParam("element_1") double n1, @FormParam("element_2") double n2, @FormParam("element_3") String s) {
        try {
            MemcachedClient client = MccFactory.getConst("localhost").getMemcachedClient();
            JsonObject innerObject = new JsonObject();
            innerObject.addProperty("key", s);
            innerObject.addProperty("firstNum",n1);
            innerObject.addProperty("secondNum",n2);

            client.add(s, 30000, innerObject.toString());
            String keys = (String) client.get("mykeys");
            keys = (keys == null) ? "" : keys;
            keys += s+"/";
            client.replace("mykeys", 30000, keys);
        } catch (Exception e) {
            e.printStackTrace();
            return getStringStackTrace(e);
        }

        return "String: " + s + ". First number = " + n1 + ", Second number = " + n2;
    }

    @DELETE
    @Path("delete")
    public String delete(@QueryParam("key") String key) {
        try {
            MemcachedClient client = MccFactory.getConst("localhost").getMemcachedClient();
            String keys = (String) client.get("mykeys");
            keys = (keys == null) ? "" : keys;
            keys = keys.replace( key+"/", "");
            client.replace("mykeys", 30000, keys);
            client.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
            return getStringStackTrace(e);
        }

        return "Removed value at " + key;
    }

    @GET
    @Path("get")
    public String get(@QueryParam("key") String key) {
        try {
            MemcachedClient client = MccFactory.getConst("localhost").getMemcachedClient();
            String res = (String) client.get(key);
            return (res != null) ? res : "No data found for " + key;
        } catch (Exception e) {
            e.printStackTrace();
            return getStringStackTrace(e);
        }


    }
    private String getStringStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
    @GET
    @Path("list")
    public String list() {
        try {
            String result = "";
            MemcachedClient client = MccFactory.getConst("localhost").getMemcachedClient();
            String s = (String) client.get("mykeys");

            String[] keys = s.split("/");
            for (String key : keys) {
                if(key==null||key.length()<1)
                    continue;
                String ss = (String) client.get(key);
                if (ss != null) result += key + " " + ss + "<br/>";
            }
            return (result.length() > 0) ? result : "No data found";
        } catch (Exception e) {
            e.printStackTrace();
            return getStringStackTrace(e);
        }

    }


    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Path("form")
    public String getForm() {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "<title>Lab 4-5 Cloud</title>\n" +
                "<style rel=\"stylesheet\" type=\"text/css\" media=\"all\">\n" +
                "body\n" +
                "{\n" +
                "\tbackground:#CDDCEC;\n" +
                "\tfont-family:\"Lucida Grande\", Tahoma, Arial, Verdana, sans-serif;\n" +
                "\tfont-size:small;\n" +
                "\tmargin:8px 0 16px;\n" +
                "\ttext-align:center;\n" +
                "}\n" +
                "\n" +
                "#form_container\n" +
                "{\n" +
                "\tbackground:#fff;\n" +
                "\t\n" +
                "\tmargin:0 auto;\n" +
                "\ttext-align:left;\n" +
                "\twidth:640px;\n" +
                "}\n" +
                "\n" +
                "#top\n" +
                "{\n" +
                "\tdisplay:block;\n" +
                "\theight:10px;\n" +
                "\tmargin:10px auto 0;\n" +
                "\twidth:650px;\n" +
                "}\n" +
                "\n" +
                "#footer\n" +
                "{\n" +
                "\twidth:640px;\n" +
                "\tclear:both;\n" +
                "\tcolor:#999999;\n" +
                "\ttext-align:center;\n" +
                "\twidth:640px;\n" +
                "\tpadding-bottom: 15px;\n" +
                "\tfont-size: 85%;\n" +
                "}\n" +
                "\n" +
                "#footer a{\n" +
                "\tcolor:#999999;\n" +
                "\ttext-decoration: none;\n" +
                "\tborder-bottom: 1px dotted #999999;\n" +
                "}\n" +
                "\n" +
                "#bottom\n" +
                "{\n" +
                "\tdisplay:block;\n" +
                "\theight:10px;\n" +
                "\tmargin:0 auto;\n" +
                "\twidth:650px;\n" +
                "}\n" +
                "\n" +
                "form.appnitro\n" +
                "{\n" +
                "\tmargin:20px 20px 0;\n" +
                "\tpadding:0 0 20px;\n" +
                "}\n" +
                "\n" +
                "/**** Logo Section  *****/\n" +
                "h1\n" +
                "{\n" +
                "\tbackground-color:#4B75B3;\n" +
                "\tmargin:0;\n" +
                "\tmin-height:0;\n" +
                "\tpadding:0;\n" +
                "\ttext-decoration:none;\n" +
                "\ttext-indent:-8000px;\n" +
                "\t\n" +
                "}\n" +
                "\n" +
                "h1 a\n" +
                "{\n" +
                "\t\n" +
                "\tdisplay:block;\n" +
                "\theight:100%;\n" +
                "\tmin-height:40px;\n" +
                "\toverflow:hidden;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "img\n" +
                "{\n" +
                "\tbehavior:url(css/iepngfix.htc);\n" +
                "\tborder:none;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "/**** Form Section ****/\n" +
                ".appnitro\n" +
                "{\n" +
                "\tfont-family:Lucida Grande, Tahoma, Arial, Verdana, sans-serif;\n" +
                "\tfont-size:small;\n" +
                "}\n" +
                "\n" +
                ".appnitro li\n" +
                "{\n" +
                "\twidth:61%;\n" +
                "}\n" +
                "\n" +
                "form ul\n" +
                "{\n" +
                "\tfont-size:100%;\n" +
                "\tlist-style-type:none;\n" +
                "\tmargin:0;\n" +
                "\tpadding:0;\n" +
                "\twidth:100%;\n" +
                "}\n" +
                "\n" +
                "form li\n" +
                "{\n" +
                "\tdisplay:block;\n" +
                "\tmargin:0;\n" +
                "\tpadding:4px 5px 2px 9px;\n" +
                "\tposition:relative;\n" +
                "}\n" +
                "\n" +
                "form li:after\n" +
                "{\n" +
                "\tclear:both;\n" +
                "\tcontent:\".\";\n" +
                "\tdisplay:block;\n" +
                "\theight:0;\n" +
                "\tvisibility:hidden;\n" +
                "}\n" +
                "\n" +
                ".buttons:after\n" +
                "{\n" +
                "\tclear:both;\n" +
                "\tcontent:\".\";\n" +
                "\tdisplay:block;\n" +
                "\theight:0;\n" +
                "\tvisibility:hidden;\n" +
                "}\n" +
                "\n" +
                ".buttons\n" +
                "{\n" +
                "\tclear:both;\n" +
                "\tdisplay:block;\n" +
                "\tmargin-top:10px;\n" +
                "}\n" +
                "\n" +
                "* html form li\n" +
                "{\n" +
                "\theight:1%;\n" +
                "}\n" +
                "\n" +
                "* html .buttons\n" +
                "{\n" +
                "\theight:1%;\n" +
                "}\n" +
                "\n" +
                "* html form li div\n" +
                "{\n" +
                "\tdisplay:inline-block;\n" +
                "}\n" +
                "\n" +
                "form li div\n" +
                "{\n" +
                "\tcolor:#444;\n" +
                "\tmargin:0 4px 0 0;\n" +
                "\tpadding:0 0 8px;\n" +
                "}\n" +
                "\n" +
                "form li span\n" +
                "{\n" +
                "\tcolor:#444;\n" +
                "\tfloat:left;\n" +
                "\tmargin:0 4px 0 0;\n" +
                "\tpadding:0 0 8px;\n" +
                "}\n" +
                "\n" +
                "form li div.left\n" +
                "{\n" +
                "\tdisplay:inline;\n" +
                "\tfloat:left;\n" +
                "\twidth:48%;\n" +
                "}\n" +
                "\n" +
                "form li div.right\n" +
                "{\n" +
                "\tdisplay:inline;\n" +
                "\tfloat:right;\n" +
                "\twidth:48%;\n" +
                "}\n" +
                "\n" +
                "form li div.left .medium\n" +
                "{\n" +
                "\twidth:100%;\n" +
                "}\n" +
                "\n" +
                "form li div.right .medium\n" +
                "{\n" +
                "\twidth:100%;\n" +
                "}\n" +
                "\n" +
                ".clear\n" +
                "{\n" +
                "\tclear:both;\n" +
                "}\n" +
                "\n" +
                "form li div label\n" +
                "{\n" +
                "\tclear:both;\n" +
                "\tcolor:#444;\n" +
                "\tdisplay:block;\n" +
                "\tfont-size:9px;\n" +
                "\tline-height:9px;\n" +
                "\tmargin:0;\n" +
                "\tpadding-top:3px;\n" +
                "}\n" +
                "\n" +
                "form li span label\n" +
                "{\n" +
                "\tclear:both;\n" +
                "\tcolor:#444;\n" +
                "\tdisplay:block;\n" +
                "\tfont-size:9px;\n" +
                "\tline-height:9px;\n" +
                "\tmargin:0;\n" +
                "\tpadding-top:3px;\n" +
                "}\n" +
                "\n" +
                "form li .datepicker\n" +
                "{\n" +
                "\tcursor:pointer !important;\n" +
                "\tfloat:left;\n" +
                "\theight:16px;\n" +
                "\tmargin:.1em 5px 0 0;\n" +
                "\tpadding:0;\n" +
                "\twidth:16px;\n" +
                "}\n" +
                "\n" +
                ".form_description\n" +
                "{\n" +
                "\tborder-bottom:1px dotted #ccc;\n" +
                "\tclear:both;\n" +
                "\tdisplay:inline-block;\n" +
                "\tmargin:0 0 1em;\n" +
                "}\n" +
                "\n" +
                ".form_description[class]\n" +
                "{\n" +
                "\tdisplay:block;\n" +
                "}\n" +
                "\n" +
                ".form_description h2\n" +
                "{\n" +
                "\tclear:left;\n" +
                "\tfont-size:160%;\n" +
                "\tfont-weight:400;\n" +
                "\tmargin:0 0 3px;\n" +
                "}\n" +
                "\n" +
                ".form_description p\n" +
                "{\n" +
                "\tfont-size:95%;\n" +
                "\tline-height:130%;\n" +
                "\tmargin:0 0 12px;\n" +
                "}\n" +
                "\n" +
                "form hr\n" +
                "{\n" +
                "\tdisplay:none;\n" +
                "}\n" +
                "\n" +
                "form li.section_break\n" +
                "{\n" +
                "\tborder-top:1px dotted #ccc;\n" +
                "\tmargin-top:9px;\n" +
                "\tpadding-bottom:0;\n" +
                "\tpadding-left:9px;\n" +
                "\tpadding-top:13px;\n" +
                "\twidth:97% !important;\n" +
                "}\n" +
                "\n" +
                "form ul li.first\n" +
                "{\n" +
                "\tborder-top:none !important;\n" +
                "\tmargin-top:0 !important;\n" +
                "\tpadding-top:0 !important;\n" +
                "}\n" +
                "\n" +
                "form .section_break h3\n" +
                "{\n" +
                "\tfont-size:110%;\n" +
                "\tfont-weight:400;\n" +
                "\tline-height:130%;\n" +
                "\tmargin:0 0 2px;\n" +
                "}\n" +
                "\n" +
                "form .section_break p\n" +
                "{\n" +
                "\tfont-size:85%;\n" +
                "\n" +
                "\tmargin:0 0 10px;\n" +
                "}\n" +
                "\n" +
                "/**** Buttons ****/\n" +
                "input.button_text\n" +
                "{\n" +
                "\toverflow:visible;\n" +
                "\tpadding:0 7px;\n" +
                "\twidth:auto;\n" +
                "}\n" +
                "\n" +
                ".buttons input\n" +
                "{\n" +
                "\tfont-size:120%;\n" +
                "\tmargin-right:5px;\n" +
                "}\n" +
                "\n" +
                "/**** Inputs and Labels ****/\n" +
                "label.description\n" +
                "{\n" +
                "\tborder:none;\n" +
                "\tcolor:#222;\n" +
                "\tdisplay:block;\n" +
                "\tfont-size:95%;\n" +
                "\tfont-weight:700;\n" +
                "\tline-height:150%;\n" +
                "\tpadding:0 0 1px;\n" +
                "}\n" +
                "\n" +
                "span.symbol\n" +
                "{\n" +
                "\tfont-size:115%;\n" +
                "\tline-height:130%;\n" +
                "}\n" +
                "\n" +
                "input.text\n" +
                "{\n" +
                "\tbackground:#fff url(../../../images/shadow.gif) repeat-x top;\n" +
                "\tborder-bottom:1px solid #ddd;\n" +
                "\tborder-left:1px solid #c3c3c3;\n" +
                "\tborder-right:1px solid #c3c3c3;\n" +
                "\tborder-top:1px solid #7c7c7c;\n" +
                "\tcolor:#333;\n" +
                "\tfont-size:100%;\n" +
                "\tmargin:0;\n" +
                "\tpadding:2px 0;\n" +
                "}\n" +
                "\n" +
                "input.file\n" +
                "{\n" +
                "\tcolor:#333;\n" +
                "\tfont-size:100%;\n" +
                "\tmargin:0;\n" +
                "\tpadding:2px 0;\n" +
                "}\n" +
                "\n" +
                "textarea.textarea\n" +
                "{\n" +
                "\tbackground:#fff url(../../../images/shadow.gif) repeat-x top;\n" +
                "\tborder-bottom:1px solid #ddd;\n" +
                "\tborder-left:1px solid #c3c3c3;\n" +
                "\tborder-right:1px solid #c3c3c3;\n" +
                "\tborder-top:1px solid #7c7c7c;\n" +
                "\tcolor:#333;\n" +
                "\tfont-family:\"Lucida Grande\", Tahoma, Arial, Verdana, sans-serif;\n" +
                "\tfont-size:100%;\n" +
                "\tmargin:0;\n" +
                "\twidth:99%;\n" +
                "}\n" +
                "\n" +
                "select.select\n" +
                "{\n" +
                "\tcolor:#333;\n" +
                "\tfont-size:100%;\n" +
                "\tmargin:1px 0;\n" +
                "\tpadding:1px 0 0;\n" +
                "\tbackground:#fff url(../../../images/shadow.gif) repeat-x top;\n" +
                "\tborder-bottom:1px solid #ddd;\n" +
                "\tborder-left:1px solid #c3c3c3;\n" +
                "\tborder-right:1px solid #c3c3c3;\n" +
                "\tborder-top:1px solid #7c7c7c;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "input.currency\n" +
                "{\n" +
                "\ttext-align:right;\n" +
                "}\n" +
                "\n" +
                "input.checkbox\n" +
                "{\n" +
                "\tdisplay:block;\n" +
                "\theight:13px;\n" +
                "\tline-height:1.4em;\n" +
                "\tmargin:6px 0 0 3px;\n" +
                "\twidth:13px;\n" +
                "}\n" +
                "\n" +
                "input.radio\n" +
                "{\n" +
                "\tdisplay:block;\n" +
                "\theight:13px;\n" +
                "\tline-height:1.4em;\n" +
                "\tmargin:6px 0 0 3px;\n" +
                "\twidth:13px;\n" +
                "}\n" +
                "\n" +
                "label.choice\n" +
                "{\n" +
                "\tcolor:#444;\n" +
                "\tdisplay:block;\n" +
                "\tfont-size:100%;\n" +
                "\tline-height:1.4em;\n" +
                "\tmargin:-1.55em 0 0 25px;\n" +
                "\tpadding:4px 0 5px;\n" +
                "\twidth:90%;\n" +
                "}\n" +
                "\n" +
                "select.select[class]\n" +
                "{\n" +
                "\tmargin:0;\n" +
                "\tpadding:1px 0;\n" +
                "}\n" +
                "\n" +
                "*:first-child+html select.select[class]\n" +
                "{\n" +
                "\tmargin:1px 0;\n" +
                "}\n" +
                "\n" +
                ".safari select.select\n" +
                "{\n" +
                "\tfont-size:120% !important;\n" +
                "\tmargin-bottom:1px;\n" +
                "}\n" +
                "\n" +
                "input.small\n" +
                "{\n" +
                "\twidth:25%;\n" +
                "}\n" +
                "\n" +
                "select.small\n" +
                "{\n" +
                "\twidth:25%;\n" +
                "}\n" +
                "\n" +
                "input.medium\n" +
                "{\n" +
                "\twidth:50%;\n" +
                "}\n" +
                "\n" +
                "select.medium\n" +
                "{\n" +
                "\twidth:50%;\n" +
                "}\n" +
                "\n" +
                "input.large\n" +
                "{\n" +
                "\twidth:99%;\n" +
                "}\n" +
                "\n" +
                "select.large\n" +
                "{\n" +
                "\twidth:100%;\n" +
                "}\n" +
                "\n" +
                "textarea.small\n" +
                "{\n" +
                "\theight:5.5em;\n" +
                "}\n" +
                "\n" +
                "textarea.medium\n" +
                "{\n" +
                "\theight:10em;\n" +
                "}\n" +
                "\n" +
                "textarea.large\n" +
                "{\n" +
                "\theight:20em;\n" +
                "}\n" +
                "\n" +
                "/**** Errors ****/\n" +
                "#error_message\n" +
                "{\n" +
                "\tbackground:#fff;\n" +
                "\tborder:1px dotted red;\n" +
                "\tmargin-bottom:1em;\n" +
                "\tpadding-left:0;\n" +
                "\tpadding-right:0;\n" +
                "\tpadding-top:4px;\n" +
                "\ttext-align:center;\n" +
                "\twidth:99%;\n" +
                "}\n" +
                "\n" +
                "#error_message_title\n" +
                "{\n" +
                "\tcolor:#DF0000;\n" +
                "\tfont-size:125%;\n" +
                "\tmargin:7px 0 5px;\n" +
                "\tpadding:0;\n" +
                "}\n" +
                "\n" +
                "#error_message_desc\n" +
                "{\n" +
                "\tcolor:#000;\n" +
                "\tfont-size:100%;\n" +
                "\tmargin:0 0 .8em;\n" +
                "}\n" +
                "\n" +
                "#error_message_desc strong\n" +
                "{\n" +
                "\tbackground-color:#FFDFDF;\n" +
                "\tcolor:red;\n" +
                "\tpadding:2px 3px;\n" +
                "}\n" +
                "\n" +
                "form li.error\n" +
                "{\n" +
                "\tbackground-color:#FFDFDF !important;\n" +
                "\tborder-bottom:1px solid #EACBCC;\n" +
                "\tborder-right:1px solid #EACBCC;\n" +
                "\tmargin:3px 0;\n" +
                "}\n" +
                "\n" +
                "form li.error label\n" +
                "{\n" +
                "\tcolor:#DF0000 !important;\n" +
                "}\n" +
                "\n" +
                "form p.error\n" +
                "{\n" +
                "\tclear:both;\n" +
                "\tcolor:red;\n" +
                "\tfont-size:10px;\n" +
                "\tfont-weight:700;\n" +
                "\tmargin:0 0 5px;\n" +
                "}\n" +
                "\n" +
                "form .required\n" +
                "{\n" +
                "\tcolor:red;\n" +
                "\tfloat:none;\n" +
                "\tfont-weight:700;\n" +
                "}\n" +
                "\n" +
                "/**** Guidelines and Error Highlight ****/\n" +
                "form li.highlighted\n" +
                "{\n" +
                "\tbackground-color:#fff7c0;\n" +
                "}\n" +
                "\n" +
                "form .guidelines\n" +
                "{\n" +
                "\tbackground:#f5f5f5;\n" +
                "\tborder:1px solid #e6e6e6;\n" +
                "\tcolor:#444;\n" +
                "\tfont-size:80%;\n" +
                "\tleft:100%;\n" +
                "\tline-height:130%;\n" +
                "\tmargin:0 0 0 8px;\n" +
                "\tpadding:8px 10px 9px;\n" +
                "\tposition:absolute;\n" +
                "\ttop:0;\n" +
                "\tvisibility:hidden;\n" +
                "\twidth:42%;\n" +
                "\tz-index:1000;\n" +
                "}\n" +
                "\n" +
                "form .guidelines small\n" +
                "{\n" +
                "\tfont-size:105%;\n" +
                "}\n" +
                "\n" +
                "form li.highlighted .guidelines\n" +
                "{\n" +
                "\tvisibility:visible;\n" +
                "}\n" +
                "\n" +
                "form li:hover .guidelines\n" +
                "{\n" +
                "\tvisibility:visible;\n" +
                "}\n" +
                "\n" +
                ".no_guidelines .guidelines\n" +
                "{\n" +
                "\tdisplay:none !important;\n" +
                "}\n" +
                "\n" +
                ".no_guidelines form li\n" +
                "{\n" +
                "\twidth:97%;\n" +
                "}\n" +
                "\n" +
                ".no_guidelines li.section\n" +
                "{\n" +
                "\tpadding-left:9px;\n" +
                "}\n" +
                "\n" +
                "/*** Success Message ****/\n" +
                ".form_success \n" +
                "{\n" +
                "\tclear: both;\n" +
                "\tmargin: 0;\n" +
                "\tpadding: 90px 0pt 100px;\n" +
                "\ttext-align: center\n" +
                "}\n" +
                "\n" +
                ".form_success h2 {\n" +
                "    clear:left;\n" +
                "    font-size:160%;\n" +
                "    font-weight:normal;\n" +
                "    margin:0pt 0pt 3px;\n" +
                "}\n" +
                "\n" +
                "/*** Password ****/\n" +
                "ul.password{\n" +
                "    margin-top:60px;\n" +
                "    margin-bottom: 60px;\n" +
                "    text-align: center;\n" +
                "}\n" +
                ".password h2{\n" +
                "    color:#DF0000;\n" +
                "    font-weight:bold;\n" +
                "    margin:0pt auto 10px;\n" +
                "}\n" +
                "\n" +
                ".password input.text {\n" +
                "   font-size:170% !important;\n" +
                "   width:380px;\n" +
                "   text-align: center;\n" +
                "}\n" +
                ".password label{\n" +
                "   display:block;\n" +
                "   font-size:120% !important;\n" +
                "   padding-top:10px;\n" +
                "   font-weight:bold;\n" +
                "}\n" +
                "\n" +
                "#li_captcha{\n" +
                "   padding-left: 5px;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "#li_captcha span{\n" +
                "\tfloat:none;\n" +
                "}\n" +
                "\n" +
                "/** Embedded Form **/\n" +
                "\n" +
                ".embed #form_container{\n" +
                "\tborder: none;\n" +
                "}\n" +
                "\n" +
                ".embed #top, .embed #bottom, .embed h1{\n" +
                "\tdisplay: none;\n" +
                "}\n" +
                "\n" +
                ".embed #form_container{\n" +
                "\twidth: 100%;\n" +
                "}\n" +
                "\n" +
                ".embed #footer{\n" +
                "\ttext-align: left;\n" +
                "\tpadding-left: 10px;\n" +
                "\twidth: 99%;\n" +
                "}\n" +
                "\n" +
                ".embed #footer.success{\n" +
                "\ttext-align: center;\n" +
                "}\n" +
                "\n" +
                ".embed form.appnitro\n" +
                "{\n" +
                "\tmargin:0px 0px 0;\n" +
                "\t\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "/*** Calendar **********************/\n" +
                "div.calendar { position: relative; }\n" +
                "\n" +
                ".calendar table {\n" +
                "cursor:pointer;\n" +
                "border:1px solid #ccc;\n" +
                "font-size: 11px;\n" +
                "color: #000;\n" +
                "background: #fff;\n" +
                "font-family:\"Lucida Grande\", Tahoma, Arial, Verdana, sans-serif;\n" +
                "}\n" +
                "\n" +
                ".calendar .button { \n" +
                "text-align: center;    \n" +
                "padding: 2px;          \n" +
                "}\n" +
                "\n" +
                ".calendar .nav {\n" +
                "background:#f5f5f5;\n" +
                "}\n" +
                "\n" +
                ".calendar thead .title { \n" +
                "font-weight: bold;      \n" +
                "text-align: center;\n" +
                "background: #dedede;\n" +
                "color: #000;\n" +
                "padding: 2px 0 3px 0;\n" +
                "}\n" +
                "\n" +
                ".calendar thead .headrow { \n" +
                "background: #f5f5f5;\n" +
                "color: #444;\n" +
                "font-weight:bold;\n" +
                "}\n" +
                "\n" +
                ".calendar thead .daynames { \n" +
                "background: #fff;\n" +
                "color:#333;\n" +
                "font-weight:bold;\n" +
                "}\n" +
                "\n" +
                ".calendar thead .name { \n" +
                "border-bottom: 1px dotted #ccc;\n" +
                "padding: 2px;\n" +
                "text-align: center;\n" +
                "color: #000;\n" +
                "}\n" +
                "\n" +
                ".calendar thead .weekend { \n" +
                "color: #666;\n" +
                "}\n" +
                "\n" +
                ".calendar thead .hilite { \n" +
                "background-color: #444;\n" +
                "color: #fff;\n" +
                "padding: 1px;\n" +
                "}\n" +
                "\n" +
                ".calendar thead .active { \n" +
                "background-color: #d12f19;\n" +
                "color:#fff;\n" +
                "padding: 2px 0px 0px 2px;\n" +
                "}\n" +
                "\n" +
                "\n" +
                ".calendar tbody .day { \n" +
                "width:1.8em;\n" +
                "color: #222;\n" +
                "text-align: right;\n" +
                "padding: 2px 2px 2px 2px;\n" +
                "}\n" +
                ".calendar tbody .day.othermonth {\n" +
                "font-size: 80%;\n" +
                "color: #bbb;\n" +
                "}\n" +
                ".calendar tbody .day.othermonth.oweekend {\n" +
                "color: #fbb;\n" +
                "}\n" +
                "\n" +
                ".calendar table .wn {\n" +
                "padding: 2px 2px 2px 2px;\n" +
                "border-right: 1px solid #000;\n" +
                "background: #666;\n" +
                "}\n" +
                "\n" +
                ".calendar tbody .rowhilite td {\n" +
                "background: #FFF1AF;\n" +
                "}\n" +
                "\n" +
                ".calendar tbody .rowhilite td.wn {\n" +
                "background: #FFF1AF;\n" +
                "}\n" +
                "\n" +
                ".calendar tbody td.hilite { \n" +
                "padding: 1px 1px 1px 1px;\n" +
                "background:#444 !important;\n" +
                "color:#fff !important;\n" +
                "}\n" +
                "\n" +
                ".calendar tbody td.active { \n" +
                "color:#fff;\n" +
                "background: #529214 !important;\n" +
                "padding: 2px 2px 0px 2px;\n" +
                "}\n" +
                "\n" +
                ".calendar tbody td.selected { \n" +
                "font-weight: bold;\n" +
                "border: 1px solid #888;\n" +
                "padding: 1px 1px 1px 1px;\n" +
                "background: #f5f5f5 !important;\n" +
                "color: #222 !important;\n" +
                "}\n" +
                "\n" +
                ".calendar tbody td.weekend { \n" +
                "color: #666;\n" +
                "}\n" +
                "\n" +
                ".calendar tbody td.today { \n" +
                "font-weight: bold;\n" +
                "color: #529214;\n" +
                "background:#D9EFC2;\n" +
                "}\n" +
                "\n" +
                ".calendar tbody .disabled { color: #999; }\n" +
                "\n" +
                ".calendar tbody .emptycell { \n" +
                "visibility: hidden;\n" +
                "}\n" +
                "\n" +
                ".calendar tbody .emptyrow { \n" +
                "display: none;\n" +
                "}\n" +
                "\n" +
                ".calendar tfoot .footrow { \n" +
                "text-align: center;\n" +
                "background: #556;\n" +
                "color: #fff;\n" +
                "}\n" +
                "\n" +
                ".calendar tfoot .ttip { \n" +
                "background: #222;\n" +
                "color: #fff;\n" +
                "font-size:10px;\n" +
                "border-top: 1px solid #dedede;\n" +
                "padding: 3px;\n" +
                "}\n" +
                "\n" +
                ".calendar tfoot .hilite { \n" +
                "background: #aaf;\n" +
                "border: 1px solid #04f;\n" +
                "color: #000;\n" +
                "padding: 1px;\n" +
                "}\n" +
                "\n" +
                ".calendar tfoot .active { \n" +
                "background: #77c;\n" +
                "padding: 2px 0px 0px 2px;\n" +
                "}\n" +
                "\n" +
                ".calendar .combo {\n" +
                "position: absolute;\n" +
                "display: none;\n" +
                "top: 0px;\n" +
                "left: 0px;\n" +
                "width: 4em;\n" +
                "border: 1px solid #ccc;\n" +
                "background: #f5f5f5;\n" +
                "color: #222;\n" +
                "font-size: 90%;\n" +
                "z-index: 100;\n" +
                "}\n" +
                "\n" +
                ".calendar .combo .label,\n" +
                ".calendar .combo .label-IEfix {\n" +
                "text-align: center;\n" +
                "padding: 1px;\n" +
                "}\n" +
                "\n" +
                ".calendar .combo .label-IEfix {\n" +
                "width: 4em;\n" +
                "}\n" +
                "\n" +
                ".calendar .combo .hilite {\n" +
                "background: #444;\n" +
                "color:#fff;\n" +
                "}\n" +
                "\n" +
                ".calendar .combo .active {\n" +
                "border-top: 1px solid #999;\n" +
                "border-bottom: 1px solid #999;\n" +
                "background: #dedede;\n" +
                "font-weight: bold;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "</style>\n" +
                "<script type=\"text/javascript\" >\n" +
                "eval(function(p,a,c,k,e,r){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--)r[e(c)]=k[c]||e(c);k=[function(e){return r[e]}];e=function(){return'\\\\w+'};c=1};while(c--)if(k[c])p=p.replace(new RegExp('\\\\b'+e(c)+'\\\\b','g'),k[c]);return p}('3(7.X){7[\"R\"+a]=a;7[\"z\"+a]=6(){7[\"R\"+a](7.1k)};7.X(\"1e\",7[\"z\"+a])}E{7.19(\"z\",a,15)}2 j=H V();6 a(){2 e=q.1d(\"1a\");3(e){o(e,\"P\");2 N=B(q,\"*\",\"14\");3((e.12<=10)||(N==\"\")){c(e,\"P\",d)}}4=B(q,\"*\",\"1n\");k(i=0;i<4.b;i++){3(4[i].F==\"1g\"||4[i].F==\"1f\"||4[i].F==\"1c\"){4[i].1b=6(){r();c(v.5.5,\"f\",d)};4[i].O=6(){r();c(v.5.5,\"f\",d)};j.D(j.b,0,4[i])}E{4[i].O=6(){r();c(v.5.5,\"f\",d)};4[i].18=6(){o(v.5.5,\"f\")}}}2 C=17.16.13();2 A=q.M(\"11\");3(C.K(\"J\")+1){c(A[0],\"J\",d)}3(C.K(\"I\")+1){c(A[0],\"I\",d)}}6 r(){k(2 i=0;i<j.b;i++){o(j[i].5.5,\"f\")}}6 B(m,y,w){2 x=(y==\"*\"&&m.Y)?m.Y:m.M(y);2 G=H V();w=w.1m(/\\\\-/g,\"\\\\\\\\-\");2 L=H 1l(\"(^|\\\\\\\\s)\"+w+\"(\\\\\\\\s|$)\");2 n;k(2 i=0;i<x.b;i++){n=x[i];3(L.1j(n.8)){G.1i(n)}}1h(G)}6 o(p,T){3(p.8){2 h=p.8.Z(\" \");2 U=T.t();k(2 i=0;i<h.b;i++){3(h[i].t()==U){h.D(i,1);i--}}p.8=h.S(\" \")}}6 c(l,u,Q){3(l.8){2 9=l.8.Z(\" \");3(Q){2 W=u.t();k(2 i=0;i<9.b;i++){3(9[i].t()==W){9.D(i,1);i--}}}9[9.b]=u;l.8=9.S(\" \")}E{l.8=u}}',62,86,'||var|if|elements|parentNode|function|window|className|_16|initialize|length|addClassName|true|_1|highlighted||_10||el_array|for|_13|_6|_c|removeClassName|_e|document|safari_reset||toUpperCase|_14|this|_8|_9|_7|load|_4|getElementsByClassName|_3|splice|else|type|_a|new|firefox|safari|indexOf|_b|getElementsByTagName|_2|onfocus|no_guidelines|_15|event_load|join|_f|_11|Array|_17|attachEvent|all|split|450|body|offsetWidth|toLowerCase|guidelines|false|userAgent|navigator|onblur|addEventListener|main_body|onclick|file|getElementById|onload|radio|checkbox|return|push|test|event|RegExp|replace|element'.split('|'),0,{}))\n" +
                "</script>\n" +
                "\n" +
                "</head>\n" +
                "<body id=\"main_body\" >\n" +
                "\t\n" +
                "\t<img id=\"top\" src=\"top.png\" alt=\"\">\n" +
                "\t<div id=\"form_container\">\n" +
                "\t\n" +
                "\t\t<h1><a>Lab 4-5a Cloud</a></h1>\n" +
                "\t\t<form id=\"form_934500\" class=\"appnitro\"  method=\"post\" action=\"/myapp/test/action\">\n" +
                "\t\t\t\t\t<div class=\"form_description\">\n" +
                "\t\t\t<h2>Lab 4a-5 Cloud</h2>\n" +
                "\t\t\t<p>© Ihor Pysmennyyi, DA-32m</p>\n" +
                "\t\t</div>\t\t\t\t\t\t\n" +
                "\t\t\t<ul >\n" +
                "\t\t\t\n" +
                "\t\t\t\t\t<li id=\"li_1\" >\n" +
                "\t\t<label class=\"description\" for=\"element_1\">First number </label>\n" +
                "\t\t<div>\n" +
                "\t\t\t<input id=\"element_1\" name=\"element_1\" class=\"element text medium\" type=\"text\" maxlength=\"255\" value=\"\"/> \n" +
                "\t\t</div><p class=\"guidelines\" id=\"guide_1\"><small>Decimal value</small></p> \n" +
                "\t\t</li>\t\t<li id=\"li_2\" >\n" +
                "\t\t<label class=\"description\" for=\"element_2\">Second number </label>\n" +
                "\t\t<div>\n" +
                "\t\t\t<input id=\"element_2\" name=\"element_2\" class=\"element text medium\" type=\"text\" maxlength=\"255\" value=\"\"/> \n" +
                "\t\t</div><p class=\"guidelines\" id=\"guide_2\"><small>Decimal value</small></p> \n" +
                "\t\t</li>\t\t<li id=\"li_3\" >\n" +
                "\t\t<label class=\"description\" for=\"element_3\">Text </label>\n" +
                "\t\t<div>\n" +
                "\t\t\t<input id=\"element_3\" name=\"element_3\" class=\"element text medium\" type=\"text\" maxlength=\"255\" value=\"\"/> \n" +
                "\t\t</div> \n" +
                "\t\t</li>\n" +
                "\t\t\t\n" +
                "\t\t\t\t\t<li class=\"buttons\">\n" +
                "\t\t\t    <input type=\"hidden\" name=\"form_id\" value=\"934500\" />\n" +
                "\t\t\t    \n" +
                "\t\t\t\t<input id=\"saveForm\" class=\"button_text\" type=\"submit\" name=\"submit\" value=\"Submit\" />\n" +
                "\t\t</li>\n" +
                "\t\t\t</ul>\n" +
                "\t\t</form>\t\n" +
                "\t\t<div id=\"footer\">\n" +
                "\t\t\tGenerated by <a href=\"http://www.phpform.org\">pForm</a>\n" +
                "\t\t</div>\n" +
                "\t</div>\n" +
                "\t<img id=\"bottom\" src=\"bottom.png\" alt=\"\">\n" +
                "\t</body>\n" +
                "</html>";
    }

//old experiments
    //@Path("{externalId}.jpg")
//    @GET
//    @Produces({"image/jpg"})
//    public Response getAsImage(@PathParam("externalId") String externalId,
//                               @Context Request request) {
//
//        System.out.println("entry");
//        // ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//        // do something with externalId, maybe retrieve an object from the
//        // db, then calculate data, size, expirationTimestamp, etc
//    /*    CacheControl cc = new CacheControl();
//        cc.setNoTransform(true);
//        cc.setMustRevalidate(false);
//        cc.setNoCache(false);
//        cc.setMaxAge(3600);*/
//
//
//        try {
//            final InputStream input = new FileInputStream("/root/rest/moto.jpg");//("/Users/ihor/Workspace/PersonaShopperREST/simple-service/src/main/java/com/example/moto.jpg");
//
//            StreamingOutput streamOut = new StreamingOutput() {
//                @Override
//                public void write(OutputStream outputStream) throws IOException, WebApplicationException {
//                    byte[] buffer = new byte[8192];
//                    int length = input.read(buffer);
//                    while (length > 0) {
//                        //logger.log(Level.WARNING, length + "");
//                        outputStream.write(buffer, 0, length);
//                        length = input.read(buffer);
//                    }
//                }
//            };
//
//            Response response = Response
//                    .ok()//.cacheControl(cc)
//                    .entity(streamOut)
//                    .build();
//            //stream.close();
//            return response;
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return Response.serverError().entity(e.toString()).build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Response.serverError().entity(e.toString()).build();
//        }
//    }
}
