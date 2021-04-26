package net.cuiwei.xiangle.utility;

import android.content.Context;

import android.util.Log;
import com.alibaba.sdk.android.oss.*;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

public class OssServiceUtil {
    private OssServiceUtil() {
    }

    private static volatile OssServiceUtil ossUtils;

    public static OssServiceUtil getInstance() {
        if (ossUtils == null) {
            synchronized (OssServiceUtil.class) {
                if (ossUtils == null) {
                    ossUtils = new OssServiceUtil();
                }
            }
        }
        return ossUtils;
    }

    //初始化使用参数
    public void init() {
    }

    /**
     * 上传本地文件
     * @param context
     * @param objectName
     * @param filePath
     * @param completedCallback
     */
    public static void putObject(Context context, String objectName, String filePath, OSSCompletedCallback<PutObjectRequest, PutObjectResult> completedCallback) {
//        OSSLog.enableLog();
        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider("https://xiangle.cuiwei.net/user/oss/sts");
        OSS oss = new OSSClient(context, "https://oss-cn-hangzhou.aliyuncs.com", credentialProvider, config());

        PutObjectRequest putObjectRequest = new PutObjectRequest("cw-test", objectName, filePath);
        OSSAsyncTask putObjectTask = oss.asyncPutObject(putObjectRequest, completedCallback);
        putObjectTask.waitUntilFinished();

//        OSSLog.disableLog();
    }

    /**
     * 上传文件内容
     * @param context
     * @param objectName
     * @param uploadData
     */
    public static void putObject(Context context, String objectName, byte[] uploadData) {
        //OSSLog.enableLog();
        OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider("https://xiangle.cuiwei.net/user/oss/sts");
        OSS oss = new OSSClient(context, "https://oss-cn-hangzhou.aliyuncs.com", credentialProvider, config());
        PutObjectRequest put = new PutObjectRequest("cw-test", objectName, uploadData);
        try {
            PutObjectResult putResult = oss.putObject(put);

            Log.d("PutObject", "UploadSuccess");
            Log.d("ETag", putResult.getETag());
            Log.d("RequestId", putResult.getRequestId());
        } catch (ClientException e) {
            // 本地异常，如网络异常等。
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常。
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        }
    }

    private static ClientConfiguration config(){
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        return conf;
    }

}