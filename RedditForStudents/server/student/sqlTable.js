
const sql = require("sql");
let studentSQL = sql.define({
    name: 'students',
    columns: [
        'id',
        'nick_name',
        'isic_number',
        'password'
    ]
});

module.exports =  {studentSQL}