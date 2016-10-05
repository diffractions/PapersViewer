package aspect;

import java.util.LinkedHashMap;

import org.aspectj.lang.ProceedingJoinPoint;

import entity.Paper;
import org.apache.log4j.Logger;

public class PaperProxy {
	private LinkedHashMap<String, Paper> paperProxyMap = new LinkedHashMap<>();
	public static Logger log = Logger.getLogger("LOG");

	public Object proxy(ProceedingJoinPoint call) throws Throwable {
		log.trace("PROXY.IN");
		
		try {

			if (call.getArgs().length > 0
					&& paperProxyMap.containsKey(call.getArgs()[0])) {
				log.info("ProxyMap" + paperProxyMap);

				return paperProxyMap.get(call.getArgs()[0]);

			}

			Object returned = call.proceed();
			if (returned instanceof Paper) {
				paperProxyMap.put(((Paper) returned).getId(), (Paper) returned);
			}
			return returned;
		} finally {
			log.trace("PROXY.OUT");
		}
	}
}
