package com.slionh.githubApi;

import com.slionh.githubApi.parse.GitThread;
import com.slionh.githubApi.parse.GitUtil;
import com.slionh.githubApi.po.Owner;
import com.slionh.githubApi.po.Repository;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/*
 * Create by s lion h on 2018/7/11
 */
public class Demo {
    @Test
    public void repositoriesDemo() {
        List<Repository> repositories=GitUtil.getRepositoryLists("s-lion-h","");
//        根据username获取repository-api，所有信息集合
        for (Repository repository:repositories){
            Owner owner=repository.getOwner();
            System.out.println(owner.getLogin()+":"+repository.getName()+
                    "\n createTime:"+repository.getCreated_at()+
                    "\n updatetime:"+repository.getUpdated_at());
        }
    }

    @Test
    public void followingDemo(){
//        根据username获取following，关注对象以owner对象返回
        List<Owner> owners=GitUtil.getFollowingList("s-lion-h","");
        for (Owner owner:owners){
            System.out.println(owner.toString());
        }
    }

    @Test
    public void getFollowingRepoDemo(){
        List<Repository> repositories=GitUtil.getFollowingRepo("echisan","");
        for (Repository repository:repositories){
            System.out.println(repository.getName()+repository.getCreated_at()+repository.getOwner().getLogin());
        }
    }

    @Test
    public void threadFollowingRepoDemo() throws ExecutionException, InterruptedException {
        List<Owner> owners=GitUtil.getFollowingList("s-lion-h","");
        List<Repository> repositories=new ArrayList<Repository>();

        System.out.println("开始执行线程");
        int taskSize = owners.size();
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(taskSize);
        // 创建多个有返回值的任务
        List<Future> list = new ArrayList<Future>();

        int taskNum=1;
        for (Owner owner:owners){
            Callable c = new GitThread(taskNum+"", owner.getLogin());
            // 执行任务并获取Future对象
            Future f = pool.submit(c);
            list.add(f);
        }
        // 关闭线程池
        pool.shutdown();
        // 获取所有并发任务的运行结果
        for (Future f : list) {
            List<Repository> repositories2= (List<Repository>) f.get();
            for (Repository repository:repositories2)
                repositories.add(repository);
            // 从Future对象上获取任务的返回值，并输出到控制台
            System.out.println(">>>" + f.get().toString());
        }
        System.out.println(repositories.toString());
    }
}
