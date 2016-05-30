package aspect;

import java.util.LinkedHashMap;

import org.aspectj.lang.ProceedingJoinPoint;

import entity.Paper;
import org.apache.log4j.Logger;

public class PaperProxy {
	private LinkedHashMap<Integer, Paper> paperProxyMap = new LinkedHashMap<>();

	public Object proxy(ProceedingJoinPoint call) throws Throwable {
		try {
			Logger.getLogger("LOG").trace("PROXY.IN");
			if (call.getArgs().length > 0
					&& paperProxyMap.containsKey(call.getArgs()[0])) {
				Logger.getLogger("LOG").info("ProxyMap" + paperProxyMap);

				return paperProxyMap.get(call.getArgs()[0]);

			}

			Object returned = call.proceed();
			if (returned instanceof Paper) {
				paperProxyMap.put(((Paper) returned).getId(), (Paper) returned);
			}
			return returned;
		} finally {
			Logger.getLogger("LOG").trace("PROXY.OUT");
		}
	}
}
