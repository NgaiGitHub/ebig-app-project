package com.ebig.net.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class test {
	public static void main(String[] args)throws Exception{
		
		 int thread_num = 50;

    	 int client_num = 2000;
    	     	
		ExecutorService exec = Executors.newCachedThreadPool();
		
		
		// 50���߳̿���ͬʱ����
		final Semaphore semp = new Semaphore( thread_num );
		// ģ��460���ͻ��˷���
        long ftime=System.currentTimeMillis(); 
        
        for( int index = 0 ; index < client_num ; index ++ )
		{

			final int NO = index;

			Runnable run = new Runnable()
			{
				public void run()
				{
					try
					{
						// ��ȡ���
						
						semp.acquire();
						long ftime1=System.currentTimeMillis(); 
						//System.out.println( "Thread:" + NO );						
						/*ִ������*/
					   	HttpClient http = new HttpClient();
				    	String usrname = "10086";
				    	String pwd = "aaa";
				  
				    	http.login(usrname, pwd); 				    					   
						HttpPacket packet = new HttpPacket();    	
						packet.setSrvtype("ModuleSetSrv");    				
						packet.setParameter(null);
						packet.setParameter("10086");
						packet.setParameter("MobileSrvMsgModuleSetSrv");
						packet.setParameter(false);
						
						packet = http.send(packet);    	
						Object obj111 = packet.getOutresult();
						System.out.println("Thread:" + NO+" ���:"+obj111);
						
						
						HttpPacket packet1 = new HttpPacket();    	
						packet1.setSrvtype("ModuleListSrv");    				
						packet1.setParameter(null);
						packet1.setParameter("10086");

						packet1 = http.send(packet1);    	
						Object obj222 = packet1.getOutresult();
						System.out.println("Thread:" + NO+" ���:"+obj222);	
						
						//http.loginout();
						//System.out.println("Thread:" + NO+" ���˳�");	
						long etime1=System.currentTimeMillis();
						System.out.println("Thread:" + NO+" ���ѵ�ʱ�� "+(etime1-ftime1)+" s");
					   /*ִ������end*/
						
						// �ͷ�
						Thread.sleep( 2000 );
						semp.release();
						
					}
					catch ( Exception e )
					{
						e.printStackTrace();
					}
				}
			};

			exec.execute( run );

		}

		// �˳��̳߳�
		long etime=System.currentTimeMillis();
		System.out.println("ͬ����ʽִ��50���������� 2000���ͷ��������ѵ�ʱ�� "+(etime-ftime)+" s");
		exec.shutdown();
         						    
    }
}
