package aspect;

import java.util.LinkedHashMap;

import org.aspectj.lang.ProceedingJoinPoint;

import entity.Paper;

public class PaperProxy {
	private LinkedHashMap<Integer, Paper> paperProxyMap = new LinkedHashMap<>();

	public Object proxy(ProceedingJoinPoint call) throws Throwable {
		try{
		System.out.println("Aspect.PROXY.IN");
		if (call.getArgs().length > 0
				&& paperProxyMap.containsKey(call.getArgs()[0])) {
			System.out.println("Aspect.ProxyMap" + paperProxyMap);
			
			return paperProxyMap.get(call.getArgs()[0]);

		}

		Object returned = call.proceed();
		if (returned instanceof Paper) {
			paperProxyMap.put(((Paper) returned).getId(), (Paper) returned);
		}
		return returned;
		}finally{
			System.out.println("Aspect.PROXY.OUT");
		}
	}
}
