package org.xutils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.xutils.common.util.KeyValue;
import org.xutils.p018db.Selector;
import org.xutils.p018db.sqlite.SqlInfo;
import org.xutils.p018db.sqlite.WhereBuilder;
import org.xutils.p018db.table.DbModel;
import org.xutils.p018db.table.TableEntity;
import org.xutils.p019ex.DbException;

public interface DbManager extends Closeable {

    public interface DbOpenListener {
        void onDbOpened(DbManager dbManager);
    }

    public interface DbUpgradeListener {
        void onUpgrade(DbManager dbManager, int i, int i2);
    }

    public interface TableCreateListener {
        void onTableCreated(DbManager dbManager, TableEntity<?> tableEntity);
    }

    void addColumn(Class<?> cls, String str) throws DbException;

    void close() throws IOException;

    int delete(Class<?> cls, WhereBuilder whereBuilder) throws DbException;

    void delete(Class<?> cls) throws DbException;

    void delete(Object obj) throws DbException;

    void deleteById(Class<?> cls, Object obj) throws DbException;

    void dropDb() throws DbException;

    void dropTable(Class<?> cls) throws DbException;

    void execNonQuery(String str) throws DbException;

    void execNonQuery(SqlInfo sqlInfo) throws DbException;

    Cursor execQuery(String str) throws DbException;

    Cursor execQuery(SqlInfo sqlInfo) throws DbException;

    int executeUpdateDelete(String str) throws DbException;

    int executeUpdateDelete(SqlInfo sqlInfo) throws DbException;

    <T> List<T> findAll(Class<T> cls) throws DbException;

    <T> T findById(Class<T> cls, Object obj) throws DbException;

    List<DbModel> findDbModelAll(SqlInfo sqlInfo) throws DbException;

    DbModel findDbModelFirst(SqlInfo sqlInfo) throws DbException;

    <T> T findFirst(Class<T> cls) throws DbException;

    DaoConfig getDaoConfig();

    SQLiteDatabase getDatabase();

    <T> TableEntity<T> getTable(Class<T> cls) throws DbException;

    void replace(Object obj) throws DbException;

    void save(Object obj) throws DbException;

    boolean saveBindingId(Object obj) throws DbException;

    void saveOrUpdate(Object obj) throws DbException;

    <T> Selector<T> selector(Class<T> cls) throws DbException;

    int update(Class<?> cls, WhereBuilder whereBuilder, KeyValue... keyValueArr) throws DbException;

    void update(Object obj, String... strArr) throws DbException;

    public static class DaoConfig {
        private boolean allowTransaction = true;
        private File dbDir;
        private String dbName = "xUtils.db";
        private DbOpenListener dbOpenListener;
        private DbUpgradeListener dbUpgradeListener;
        private int dbVersion = 1;
        private TableCreateListener tableCreateListener;

        public DaoConfig setDbDir(File dbDir2) {
            this.dbDir = dbDir2;
            return this;
        }

        public DaoConfig setDbName(String dbName2) {
            if (!TextUtils.isEmpty(dbName2)) {
                this.dbName = dbName2;
            }
            return this;
        }

        public DaoConfig setDbVersion(int dbVersion2) {
            this.dbVersion = dbVersion2;
            return this;
        }

        public DaoConfig setAllowTransaction(boolean allowTransaction2) {
            this.allowTransaction = allowTransaction2;
            return this;
        }

        public DaoConfig setDbOpenListener(DbOpenListener dbOpenListener2) {
            this.dbOpenListener = dbOpenListener2;
            return this;
        }

        public DaoConfig setDbUpgradeListener(DbUpgradeListener dbUpgradeListener2) {
            this.dbUpgradeListener = dbUpgradeListener2;
            return this;
        }

        public DaoConfig setTableCreateListener(TableCreateListener tableCreateListener2) {
            this.tableCreateListener = tableCreateListener2;
            return this;
        }

        public File getDbDir() {
            return this.dbDir;
        }

        public String getDbName() {
            return this.dbName;
        }

        public int getDbVersion() {
            return this.dbVersion;
        }

        public boolean isAllowTransaction() {
            return this.allowTransaction;
        }

        public DbOpenListener getDbOpenListener() {
            return this.dbOpenListener;
        }

        public DbUpgradeListener getDbUpgradeListener() {
            return this.dbUpgradeListener;
        }

        public TableCreateListener getTableCreateListener() {
            return this.tableCreateListener;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            DaoConfig daoConfig = (DaoConfig) o;
            if (!this.dbName.equals(daoConfig.dbName)) {
                return false;
            }
            if (this.dbDir != null) {
                return this.dbDir.equals(daoConfig.dbDir);
            }
            if (daoConfig.dbDir != null) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (this.dbName.hashCode() * 31) + (this.dbDir != null ? this.dbDir.hashCode() : 0);
        }

        public String toString() {
            return String.valueOf(this.dbDir) + "/" + this.dbName;
        }
    }
}
