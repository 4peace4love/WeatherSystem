//时间戳转时间
function timestamp2date(sj)
{
    var now = new Date(sj);
    var   year=now.getFullYear();
    var   month=now.getMonth()+1;
    var   date=now.getDate();
    return   year+"-"+month+"-"+date;

}