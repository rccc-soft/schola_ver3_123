package com.example.schola_ver3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RuleDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "rule.db";

    private static final String TABLE_NAME = "rule";
    private static final String RULE_ID = "rule_id";
    private static final String RULE_CONTENT = "rule_content";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    RULE_ID + " INTEGER PRIMARY KEY," +
                    RULE_CONTENT + " TEXT NOT NULL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    RuleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //データベースが初めて作成されるときに呼び出される
    //メソッド内でテーブルを作成する
    @Override
    public void onCreate(SQLiteDatabase db) {
        //テーブル作成
        db.execSQL(SQL_CREATE_ENTRIES);
        setValue(db);
    }

    //rule_id = 2：配送業者利用規約まだ
    private void setValue(SQLiteDatabase db){
        ContentValues values1 = new ContentValues();
        values1.put("rule_id", 1);
        values1.put("rule_content", "この利用規約（以下「本規約」）は、フリマアプリ「スコラ」（以下「当アプリ」）を利用するすべてのユーザー（以下「ユーザー」）に適用されます。ユーザーは当アプリを使用することにより、本規約に同意したものとみなされます。\n" +
                "1. 利用資格\n" +
                "\n" +
                "1.1 当アプリは、学生専用のフリマアプリです。ユーザーは学生でなければならず、一般の成人（学生でない者）は利用できません。\n" +
                "1.2 学生とは、以下の条件を満たす者を指します：\n" +
                "\n" +
                "    中学生、高校生、大学生、またはそれに準ずる教育機関に在籍している者\n" +
                "    1.3 中学生（13歳以上16歳未満）のユーザーが当アプリを利用する場合、親または法定代理人の認証が必要です。親または法定代理人の同意を得た上で、親または法定代理人によるアカウント認証を完了することが求められます。\n" +
                "    1.4 ユーザーは、正確かつ最新の情報を提供し、アカウントを登録する必要があります。\n" +
                "\n" +
                "2. アカウントの登録\n" +
                "\n" +
                "2.1 ユーザーは、当アプリの利用に際して、必要な情報を提供し、アカウントを登録することが求められます。\n" +
                "2.2 学生であることを証明するため、ユーザーは学籍番号や学校名を提供する必要がある場合があります。\n" +
                "2.3 中学生のユーザーがアカウントを登録する場合、親または法定代理人の認証を受けなければなりません。認証を完了することで、アカウントが有効になります。\n" +
                "2.4 ユーザーは、アカウント情報の管理および安全性について責任を負います。\n" +
                "2.5 アカウント情報の譲渡や貸与はできません。\n" +
                "3. 商品出品および取引\n" +
                "\n" +
                "3.1 出品者は、商品が実際に存在し、説明と一致していることを保証します。\n" +
                "3.2 購入者は、購入後速やかに支払いを完了するものとし、取引を円滑に進める義務があります。\n" +
                "3.3 出品者は、購入者が商品を購入後、速やかに発送するものとします。\n" +
                "3.4 商品が発送された後、取引は完了と見なされます。\n" +
                "4. 禁止行為\n" +
                "\n" +
                "ユーザーは、以下の行為を行ってはならないものとします：\n" +
                "\n" +
                "    偽りの情報を提供すること\n" +
                "    不正な方法で商品を出品すること（偽物、盗品、不正な商品など）\n" +
                "    他のユーザーに対して不快な行為や言葉を使用すること\n" +
                "    当アプリの運営を妨害する行為\n" +
                "    取引相手を騙す行為や不正な利益を得る行為\n" +
                "\n" +
                "5. 取引のキャンセルおよび返金\n" +
                "\n" +
                "5.1 取引が完了する前に、購入者または出品者は取引をキャンセルすることができます。\n" +
                "5.2 商品に瑕疵があった場合、購入者は受け取り後7日以内に返金を請求することができます。\n" +
                "6. 個人情報の取り扱い\n" +
                "\n" +
                "6.1 ユーザーの個人情報は、当アプリの運営および取引に必要な範囲内で収集、使用されます。\n" +
                "6.2 個人情報の提供を拒否することはできますが、その場合は一部サービスが利用できないことがあります。\n" +
                "7. 著作権\n" +
                "\n" +
                "7.1 当アプリ内で提供されるすべてのコンテンツ（テキスト、画像、ロゴなど）の著作権は、当アプリまたは権利者に帰属します。\n" +
                "7.2 ユーザーは、当アプリ内のコンテンツを無断で複製、転載、改変することはできません。\n" +
                "8. サービスの変更・停止\n" +
                "\n" +
                "8.1 当アプリは、事前の通知なしにサービスの内容を変更、停止、または終了することがあります。\n" +
                "8.2 サービスの停止によってユーザーに生じた損害について、当アプリは一切の責任を負いません。\n" +
                "9. 免責事項\n" +
                "\n" +
                "9.1 当アプリは、ユーザー同士の取引に関して一切の責任を負いません。\n" +
                "9.2 天災、システム障害、またはその他不可抗力によりサービスが提供できなかった場合、当アプリは責任を負いません。\n" +
                "10. 利用規約の改定\n" +
                "\n" +
                "10.1 本規約は、当アプリが必要と判断した場合、予告なく改定されることがあります。\n" +
                "10.2 改定後の規約は、当アプリ上での公開をもって効力を生じます。\n" +
                "11. 準拠法および裁判管轄\n" +
                "\n" +
                "11.1 本規約は、日本の法律に準拠します。\n" +
                "11.2 本規約に関連して発生した紛争は、東京地方裁判所を第一審の専属的裁判管轄裁判所とします。");
        db.insert("rule", null, values1);

        ContentValues values2 = new ContentValues();
        values2.put("rule_id", 2);
        values2.put("rule_content", "配送業者利用規約（以下「本規約」）は、フリマアプリ「スコラ」（以下「当アプリ」）において、配送業者としてサービスを提供する個人および法人に適用されます。\n" +
                "1. 配送業者登録\n" +
                "\n" +
                "1.1 配送業者は、正確な情報を提供し、当アプリに登録を行う必要があります。\n" +
                "1.2 配送業者として登録するには、本人確認書類および事業登録証明書の提出が必要です。\n" +
                "1.3 不正な情報を提供した場合、登録が取り消される可能性があります。\n" +
                "2. 配送義務\n" +
                "\n" +
                "2.1 配送業者は、購入者および出品者に対し、指定された期間内に配送を行う責任があります。\n" +
                "2.2 配送の遅延が発生した場合、速やかに当アプリおよび関係者に通知し、適切な対応を取る必要があります。\n" +
                "3. 配送中の損害および紛失\n" +
                "\n" +
                "3.1 配送中に発生した商品の損害または紛失について、配送業者は責任を負います。\n" +
                "3.2 損害または紛失が発生した場合、配送業者は速やかに関係者および当アプリに報告し、補償手続きを行う必要があります。\n" +
                "（以下省略）");
        db.insert("rule", null, values2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db); //再作成
    }

    //データベースを古いバージョンに戻すときに呼び出される
//    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public String getRuleContent(SQLiteDatabase db, int ruleId) {
        // データ取得
        Cursor cursor = db.query(
                "rule", // テーブル名
                new String[]{"rule_content"}, // 取得する列
                "rule_id = ?", // WHERE句
                new String[]{String.valueOf(ruleId)}, // WHERE句の条件値
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("rule_content");
            if (columnIndex != -1) {
                String ruleContent = cursor.getString(columnIndex);
                cursor.close();
                return ruleContent;
            } else {
                Log.e("Database", "'rule_content' column not found.");
                cursor.close();
                return null;
            }
        } else {
            Log.e("Database", "Cursor is empty or invalid.");
            return null;
        }
    }
}