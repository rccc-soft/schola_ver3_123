package com.example.schola_ver3;

//  会員者情報削除のデータベース追加項目
public boolean deleteMember(String memberId) {
    SQLiteDatabase db = this.getWritableDatabase();
    int rowsDeleted = db.delete(TABLE_MEMBERS, COLUMN_MEMBER_ID + " = ?", new String[]{memberId});
    return rowsDeleted > 0;
}

