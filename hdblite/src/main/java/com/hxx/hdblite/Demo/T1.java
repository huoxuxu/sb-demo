package com.hxx.hdblite.Demo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class T1 {
    private Map<String, Object> updateDic = new HashMap<String, Object>();

    public Map<String, Object> GetUpdateDic() {
        Map<String, Object> dic = new HashMap<>();
        for (Map.Entry<String, Object> entry : this.updateDic.entrySet()) {
            String key = entry.getKey();
            String name = _Dic.get(key);

            dic.put(name, entry.getValue());
        }

        return dic;
    }

    private void a() {
    }

    private static void a1() {
    }

    private int ID;
    private String Name;
    private String Name2;
    private double Score;
    private int Age;
    private String Remark;
    private Date CreateDate;
    private Date CreateTime;
    private Date UpdateDate;

    private static Map<String, String> _Dic = new HashMap<String, String>();

    static {
        _Dic.put("ID", "ID");
        _Dic.put("Name", "Name");
        _Dic.put("Name2", "Name2");
        _Dic.put("Score", "Score");
        _Dic.put("Age", "Age");
        _Dic.put("Remark", "Remark");
        _Dic.put("CreateDate", "CreateDate");
        _Dic.put("CreateTime", "CreateTime");
        _Dic.put("UpdateDate", "UpdateDate");
    }

    public class _ {
        public final static String ID = "ID";
        public final static String Name = "Name";
        public final static String Name2 = "Name2";
        public final static String Score = "Score";
        public final static String Age = "Age";
        public final static String Remark = "Remark";
        public final static String CreateDate = "CreateDate";
        public final static String CreateTime = "CreateTime";
        public final static String UpdateDate = "UpdateDate";
    }

    //get set
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
        updateDic.put("ID", this.ID);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
        updateDic.put("Name", this.Name);
    }

    public String getName2() {
        return Name2;
    }

    public void setName2(String name2) {
        Name2 = name2;
        updateDic.put("Name2", this.Name2);
    }

    public double getScore() {
        return Score;
    }

    public void setScore(double score) {
        Score = score;
        updateDic.put("Score", this.Score);
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
        updateDic.put("Age", this.Age);
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
        updateDic.put("Remark", this.Remark);
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
        updateDic.put("CreateDate", this.CreateDate);
    }

    public Date getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Date createTime) {
        CreateTime = createTime;
        updateDic.put("CreateTime", this.CreateTime);
    }

    public Date getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(Date updateDate) {
        UpdateDate = updateDate;
        updateDic.put("UpdateDate", this.UpdateDate);
    }
}
