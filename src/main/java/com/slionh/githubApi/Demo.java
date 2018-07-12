package com.slionh.githubApi;

import com.slionh.githubApi.parse.GitUtil;
import com.slionh.githubApi.po.Owner;
import com.slionh.githubApi.po.Repository;
import org.junit.Test;

import java.util.List;

/*
 * Create by s lion h on 2018/7/11
 */
public class Demo {
    @Test
    public void repositoriesDemo() {
        List<Repository> repositories=GitUtil.getRepositoryLists("s-lion-h");
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
        List<Owner> owners=GitUtil.getFollowingList("s-lion-h");
        for (Owner owner:owners){
            System.out.println(owner.toString());
        }
    }

    @Test
    public void getFollowingRepoDemo(){
        List<Repository> repositories=GitUtil.getFollowingRepo("echisan");
        for (Repository repository:repositories){
            System.out.println(repository.getName()+repository.getCreated_at()+repository.getOwner().getLogin());
        }
//        GitUtil.getFollowingRepo("echisan");
//        List<Repository> repositories=GitUtil.getFollowingRepo("echisan");
    }
}
