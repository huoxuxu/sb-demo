package com.hxx.hdblite.Models;

import com.hxx.hdblite.tools.ModelTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表示查询到的一行数据，
 * 必须保证列和值的个数一致
 */
public class DbRow {
    private List<DbColumn> Cols = new ArrayList<>();
    private Object[] ItemArray;

    public DbRow(List<DbColumn> cols, Object[] itemArray) {
        Cols = cols;
        ItemArray = itemArray;
    }

    //Static========================================

    /**
     * 将DbRow转为字典
     *
     * @return
     */
    public Map<String, Object> ToDic() {
        Map<String, Object> dic = new HashMap<>();

        int colCou = this.Cols.size();
        for (int i = 0; i < colCou; i++) {
            DbColumn col = this.Cols.get(i);
            String colName = col.getName();

            //如果包含相同的列，跳过！！！
            if (dic.containsKey(colName)) continue;

            Object val = this.ItemArray[i];
            dic.put(colName, val);
        }

        return dic;
    }

    /**
     * 将DbRow转为T，优先根据T中属性匹配
     *
     * @param <T>
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public <T> T ToEntity(Class clazz) throws Exception {
        Map<String, Object> dic = ToDic();

        return ModelTools.ToEntity(dic, clazz);
    }

    //get set-----------
    public List<DbColumn> getCols() {
        return Cols;
    }

    public Object[] getItemArray() {
        return ItemArray;
    }

    public int getCount() {
        return this.ItemArray.length;
    }

}
