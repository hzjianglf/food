<%@ WebHandler Language="C#" Class="WizardWuUpload" %>
using System;
using System.Web;
using System.IO;
//using System.Runtime.Serialization;
//using System.Runtime.Serialization.Json;

public class WizardWuUpload : IHttpHandler {
    private const string strUploadFolder = "~/uploadFiles/";    //圖片上傳後的存放路徑
    
    public void ProcessRequest (HttpContext context) {
        /*
         * 作者: WizardWu
         * http://www.cnblogs.com/WizardWu
         * http://www.facebook.com/DBtaoist
         * 日期: 2015/02/19
         * 版本: v1.1
         */
        //參考資料:
        //Asp.Net 用Jquery和一般处理程序实现无刷新上传大文件:http://www.cnblogs.com/zhongweiv/archive/2013/04/16/upload_without_refresh.html
        //此書8-11節(部分代碼取自8_12):http://www.broadview.com.cn/23949
        //HttpPostedFile 类:https://msdn.microsoft.com/zh-tw/library/system.web.httppostedfile%28v=vs.110%29.aspx
        //HttpFileCollection 类:https://msdn.microsoft.com/zh-cn/library/system.web.httpfilecollection(v=vs.110).aspx
        //其他书籍、网路上的范例
                
        //context.Response.ContentType = "text/plain";
        //context.Response.Write("Hello World");
        //string[] arrExts = { ".jpg", ".png", ".gif", ".jpeg", ".bmp" };
        
        //上傳圖片 or 刪除圖片
        if (string.IsNullOrEmpty(context.Request.QueryString["act"])) //上傳圖片
        {
            string oriFileName = ""; //原圖
            string newFileName = ""; //截圖後的新圖
            string FileExt = "";     //檢查扩展名
            int intFileSize = 0;     //檢查大小(KB)
            string strReturn = "";   //返回給前台的值 @"{""Name"" : ""值"", ""NewName"" : ""值"", ""Size"" : ""值"", ""Result"" : ""值""}";  //json
            int resultVal = (int)ReturnVal.Failed;

            try
            {
                //HttpPostedFile myFile = context.Request.Files["NameForCSharpHandler1"];
                //HttpPostedFile myFile = context.Request.Files["NameForCSharpHandler2"];
                HttpPostedFile myFile = context.Request.Files[0];
                
                intFileSize = (myFile.ContentLength / 1024);    //Byte/1024=KB。 若 > 1048576  //圖片不可大於 1024*1024=1048576 (1 MB)   //50*1024=51200 (50 KB)
                //long ii = myFile.InputStream.Length;  //內容同 myFile.ContentLength

                if (myFile != null)
                {
                    if (myFile.InputStream.Length != 0)
                    {
                        if (myFile.InputStream.Length < 1048576) //若小於 1 MB，才允許上傳
                        {
                            FileExt = Path.GetFileNameWithoutExtension(myFile.FileName);
                            FileExt = Path.GetExtension(myFile.FileName);

                            if (FileExt.Equals(".jpg") || FileExt.Equals(".jpeg") || FileExt.Equals(".png") || FileExt.Equals(".gif") || FileExt.Equals(".bmp"))
                            {
                                oriFileName = Path.GetFileName(myFile.FileName);     //原始文件名 (仅含文件名、扩展名，不含用户电脑里文件的路径)
                                //myFile.FileName;   //包含用户电脑里文件的路径、文件名、扩展名，如: D:\\myFile.jpg
                                //newFileName = string.Format("{0}_{1}", Guid.NewGuid(), originalFileName);   //新文件名---组成形式：  GUID + 下划线 + 原文件名
                                newFileName = Path.GetFileNameWithoutExtension(myFile.FileName) + "_cut" + Path.GetExtension(myFile.FileName); //自訂一個新的文件名

                                //string fileAbsPath = context.Server.MapPath(strUploadFolder) + newFileName;   //绝对路径 (要存放的路徑+文件名)
                                myFile.SaveAs(context.Server.MapPath(strUploadFolder) + oriFileName);       //上传并存成原始文件名的文件 (原圖)
                                //myFile.SaveAs(context.Server.MapPath(strUploadFolder) + newFileName);     //上传并存成新文件名的文件 (截圖後的新圖)

                                resultVal = (int)ReturnVal.Succeed; //1:成功
                            }
                            else
                            {
                                resultVal = (int)ReturnVal.ExtNotMatch; //-4:上传的扩展名不符合
                            }
                        }
                        else
                        {
                            resultVal = (int)ReturnVal.TooBig;  //-3:不能上传大於 1 MB 的文件
                        }
                    }
                    else
                    {
                        resultVal = (int)ReturnVal.FileEmpty;   //-2:不能上传 0 KB 大小的文件
                    }
                }
                else
                {
                    resultVal = (int)ReturnVal.NotSelected; //-1:未選擇文件
                }
            }
            catch (Exception)
            {
                resultVal = (int)ReturnVal.Failed;  //0:上传失败
                //可在此寫 Log
            }
            finally
            {
                //回傳 json 格式的內容給前端 jQuery
                strReturn = @"{""Name"" : """ + oriFileName + @""", ""NewName"" : """ + newFileName +
                    @""", ""Size"" : """ + intFileSize.ToString() + @""", ""Result"" : """ + resultVal.ToString() + @"""}";
                context.Response.Write(strReturn);

                //string ss = @"{""Name"" : ""值"", ""NewName"" : ""值"", ""Size"" : ""值"", ""Result"" : ""值""}";  //json
                //context.Response.Write(ss);  //返回值 (可為 json 或數字)

                //context.Response.Write(arrReturn);    //返回值(回傳Array，會引發前台jQuery的error)
                //context.Response.Write("test");       //返回值(回傳String，會引發前台jQuery的error)
            }
        }
        else //刪除圖片
        {
            string strNewNameDelete = context.Request.Form["NewNameDelete"].ToString();     //150128-02_cut.jpg (截圖後的新圖)
            string strNameDelete = context.Request.Form["NameDelete"].ToString();           //150128-02.jpg (原圖)

            //Server.MapPath(): 转为服务器上硬盘里的绝对路径
            if (File.Exists(context.Server.MapPath(strUploadFolder) + strNameDelete))
            {
                try
                {
                    File.Delete(context.Server.MapPath(strUploadFolder) + strNameDelete);           //原圖

                    //if (File.Exists(context.Server.MapPath(strUploadFolder) + strNewNameDelete))
                    //    File.Delete(context.Server.MapPath(strUploadFolder) + strNewNameDelete);  //截圖後的新圖
                    
                    context.Response.Write("1");  //刪除成功。 返回值可為 json 或數字    
                }
                catch
                {
                    context.Response.Write("0");  //刪除失敗！刪除時發生錯誤。 返回值可為 json 或數字   
                }
            }
            else
            {
                context.Response.Write("-5");  //刪除失敗！文件不存在。 返回值可為 json 或數字
            }
        }
        
    } //end of ProcessRequest

    #region 返回值
    /// <summary>
    /// 返回值
    /// </summary>
    private enum ReturnVal : int
    {
        /// <summary>
        /// 不能上传 0 KB 大小的文件
        /// </summary>
        FileEmpty = -2,

        /// <summary>
        /// 不能上传大於 1 MB 的文件
        /// </summary>
        TooBig = -3,

        /// <summary>
        /// 上传的扩展名不符合
        /// </summary>
        ExtNotMatch = -4,

        /// <summary>
        /// 未選擇文件
        /// </summary>
        NotSelected = -1,

        /// <summary>
        /// 上传失败
        /// </summary>
        Failed = 0,

        /// <summary>
        /// 成功
        /// </summary>
        Succeed = 1
    }
    #endregion
 
    public bool IsReusable {
        get {
            return false;
        }
    }

}