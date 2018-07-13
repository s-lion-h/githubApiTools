package com.slionh.githubApi.parse;

import com.slionh.githubApi.po.Repository;

import java.util.List;
import java.util.concurrent.Callable;

/*
 * Create by s lion h on 2018/7/13
 */
public class GitThread implements Callable<Object> {
    private String taskNum;
    private String username;
    private String token="";

    public GitThread(String taskNum) {
        this.taskNum = taskNum;
    }

    public GitThread(String taskNum, String username) {
        this.taskNum = taskNum;
        this.username = username;
    }

    public GitThread(String taskNum, String username, String token) {
        this.taskNum = taskNum;
        this.username = username;
        this.token = token;
    }

    public Object call() throws Exception {
        GitUtil gitUtil=new GitUtil();
//        List<Repository> repositories= GitUtil.getRepositoryLists(username,token);
        List<Repository> repositories= gitUtil.getRepositoryLists(username,token);
        return  repositories;
    }
}
