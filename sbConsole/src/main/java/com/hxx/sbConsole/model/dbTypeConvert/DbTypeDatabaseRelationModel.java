package com.hxx.sbConsole.model.dbTypeConvert;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-05-24 16:44:40
 **/
@Data
@AllArgsConstructor
public class DbTypeDatabaseRelationModel implements Serializable {
    private static final long serialVersionUID = -1410609266732106623L;

    private String DbType;
    private DatabaseTypeEnum DatabaseType;
    private String DatabaseField;
    private boolean Recommend;

    public static List<DbTypeDatabaseRelationModel> getData() {
        List<DbTypeDatabaseRelationModel> ls = new ArrayList<>();
        {
            ls.add(new DbTypeDatabaseRelationModel("Int64", DatabaseTypeEnum.MySQL, "BIGINT",false));
        }
        return ls;
    }

    public enum DatabaseTypeEnum{
        MySQL,
        SQLite,
        OracleDB,
        PGSQL,
    }
}
