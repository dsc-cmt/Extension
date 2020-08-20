package io.github.cmt.extension.core.filter;

import static io.github.cmt.extension.common.BusinessContext.BIZ_CODE_KEY;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import io.github.cmt.extension.common.BusinessContext;

import org.springframework.util.StringUtils;

/**
 * dubbo调用时 消费者将bizcode从传到dubbo rpccontext
 *
 * @author tuzhenxian
 * @date 20-8-10
 */
@Activate(group = {Constants.CONSUMER})
public class ConsumerBizCodeFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String bizCode = BusinessContext.getBizCode();
        if (!StringUtils.isEmpty(bizCode)) {
            RpcContext.getContext().getAttachments().put(BIZ_CODE_KEY, bizCode);
        }
        return invoker.invoke(invocation);
    }
}
