package com.lzy.okgo.p000db;

import java.util.ArrayList;
import java.util.List;

/* renamed from: com.lzy.okgo.db.TableEntity */
public class TableEntity {
    private List<ColumnEntity> list = new ArrayList();
    public String tableName;

    public TableEntity(String tableName2) {
        this.tableName = tableName2;
    }

    public TableEntity addColumn(ColumnEntity columnEntity) {
        this.list.add(columnEntity);
        return this;
    }

    public String buildTableString() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(this.tableName).append('(');
        for (ColumnEntity entity : this.list) {
            if (entity.compositePrimaryKey != null) {
                sb.append("PRIMARY KEY (");
                for (String primaryKey : entity.compositePrimaryKey) {
                    sb.append(primaryKey).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append(")");
            } else {
                sb.append(entity.columnName).append(" ").append(entity.columnType);
                if (entity.isNotNull) {
                    sb.append(" NOT NULL");
                }
                if (entity.isPrimary) {
                    sb.append(" PRIMARY KEY");
                }
                if (entity.isAutoincrement) {
                    sb.append(" AUTOINCREMENT");
                }
                sb.append(",");
            }
        }
        if (sb.toString().endsWith(",")) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(')');
        return sb.toString();
    }

    public String getColumnName(int columnIndex) {
        return this.list.get(columnIndex).columnName;
    }

    public int getColumnCount() {
        return this.list.size();
    }

    public int getColumnIndex(String columnName) {
        int columnCount = getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            if (this.list.get(i).columnName.equals(columnName)) {
                return i;
            }
        }
        return -1;
    }
}
