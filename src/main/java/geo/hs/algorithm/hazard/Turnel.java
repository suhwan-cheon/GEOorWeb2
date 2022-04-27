package geo.hs.algorithm.hazard;

import geo.hs.model.hazard.Hazard;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Turnel {
    public static ArrayList<Hazard> run() throws IOException, ParseException {
        //교량 (고양시 덕양구, 강원도 강릉, 대관령)
        String code1[] = {"2", "1", "2", "2", "3", "3"};
        String code2[] = {"41", "02", "42", "42", "01", "01"};
        String code3[] = {"41289", "00022", "42002", "42150", "01202", "01205"};
        int cnt = 6;
        ArrayList<Hazard> hz = new ArrayList<Hazard>();

        for(int i=0; i<cnt; i++) {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1613000/btiData/getTunlList"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=mWqQaDoKuEgZ6uOuwh6YVaxSjmlymrAML5TELthV%2FpJi9FHf4fYDL5O4VnQWTf1ks3eWxySwiNQF%2FnC0Nh1kWg%3D%3D"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("responseType", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*데이터형식(xml/json)*/
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한페이지결과수*/
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
            urlBuilder.append("&" + URLEncoder.encode("hyear", "UTF-8") + "=" + URLEncoder.encode("2020", "UTF-8")); /*조회할기준년도*/
            urlBuilder.append("&" + URLEncoder.encode("roadType", "UTF-8") + "=" + URLEncoder.encode("20", "UTF-8")); /*도로종류코드*/
            urlBuilder.append("&" + URLEncoder.encode("manType", "UTF-8") + "=" + URLEncoder.encode(code1[i], "UTF-8")); /*기관구분1 코드*/
            urlBuilder.append("&" + URLEncoder.encode("manOwn", "UTF-8") + "=" + URLEncoder.encode(code2[i], "UTF-8")); /*기관구분2 코드*/
            urlBuilder.append("&" + URLEncoder.encode("manManage", "UTF-8") + "=" + URLEncoder.encode(code3[i], "UTF-8")); /*기관구분3 코드*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            String jsonStr = sb.toString();

            JSONParser parser = new JSONParser();
            Object obj = parser.parse( jsonStr );
            JSONObject jsonObj = (JSONObject) obj;

            Object resp = jsonObj.get("response");
            JSONObject respObj = (JSONObject) resp;
            Object resp2 = respObj.get("body");
            JSONObject respObj2 = (JSONObject) resp2;
            Object resp3 = respObj2.get("items");


            for (Object o: (JSONArray) resp3){
                JSONObject jo = (JSONObject) o;
                Hazard hazard = new Hazard(Double.parseDouble(jo.get("sLatitude").toString()), Double.parseDouble(jo.get("sLongitude").toString()));
                hz.add(hazard);
                hazard = new Hazard(Double.parseDouble(jo.get("eLatitude").toString()), Double.parseDouble(jo.get("eLongitude").toString()));
                hz.add(hazard);
            }
        }

        return hz;
    }
}