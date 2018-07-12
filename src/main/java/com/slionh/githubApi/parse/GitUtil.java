package com.slionh.githubApi.parse;

import com.slionh.githubApi.po.Owner;
import com.slionh.githubApi.po.Repository;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * Create by s lion h on 2018/7/11
 */
public class GitUtil {
    private static final String URL_HEAD="https://api.github.com/users/";
    private static String TOKEN="?access_token=";

    public static List<Repository> getFollowingRepo(String username,String token){
        List<Owner> owners=getFollowingList(username,token);
        List<Repository> repositories=new ArrayList<Repository>();
        for (Owner owner:owners){
//            System.out.println(owner.getLogin());
            List<Repository> repositoriesOwner=getRepositoryLists(owner.getLogin(),token);
            for (Repository repository:repositoriesOwner){
                repositories.add(repository);
//                System.out.println(repository.getName());
            }
//            System.out.println("------------------------------------");
        }
        return repositories;
    }

    public static List<Owner> getFollowingList(String username,String token){
        if (TOKEN.equals("?access_token=")){
            TOKEN=TOKEN+token;
        }
        GitUtil gitUtil=new GitUtil();
        String content=gitUtil.getFollowing(username);
        List<Owner> owners=gitUtil.getFollowingOwnerObject(content);
        return owners;
    }

    public static List<Repository> getRepositoryLists(String username,String token){
        if (TOKEN.equals("?access_token=")){
            TOKEN=TOKEN+token;
        }
        GitUtil gitUtil=new GitUtil();
        String content=gitUtil.getRepositories(username);
        List<Repository> repositories=gitUtil.getRepositoriesObject(content);
        return repositories;
    }

//    获取git个人respositories数据
    public String getRepositories(String username){
        String content="";
        String url=URL_HEAD+username+"/repos"+TOKEN;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        content=getString(url, httpclient);
        return content;
    }

//    创建httpGet请求获取response
    private String getString(String url, CloseableHttpClient httpclient) {
        String content="";
        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet(url);
            System.out.println("------------------------------------");
//            httpget.setHeader("Cookie",COOKIES);
            System.out.println("executing request " + httpget.getURI());
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    // 打印响应内容长度
                    System.out.println("Response content length: " + entity.getContentLength());
                    content=EntityUtils.toString(entity);
                }
                System.out.println("------------------------------------");
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

//    封装响应Repositories-Json为List<Repository>
    public List<Repository> getRepositoriesObject(String responseContent){
        List<Repository> repositories=new ArrayList<Repository>();
        JSONArray jsonArray=new JSONArray(responseContent);
        int length=jsonArray.length();

        for (int i=0;i<length;i++){
            Owner owner;
            Repository repository;

            JSONObject jsonObject=jsonArray.getJSONObject(i);
            JSONObject ownerObject=jsonObject.getJSONObject("owner");
            owner=new Owner(ownerObject.getString("login") ,
                    ownerObject.getInt("id"),
                    ownerObject.getString("node_id") ,
                    ownerObject.getString("avatar_url") ,
                    ownerObject.getString("gravatar_id") ,
                    ownerObject.getString("url") ,
                    ownerObject.getString("html_url") ,
                    ownerObject.getString("followers_url") ,
                    ownerObject.getString("following_url") ,
                    ownerObject.getString("gists_url") ,
                    ownerObject.getString("starred_url") ,
                    ownerObject.getString("subscriptions_url") ,
                    ownerObject.getString("organizations_url") ,
                    ownerObject.getString("repos_url") ,
                    ownerObject.getString("events_url") ,
                    ownerObject.getString("received_events_url") ,
                    ownerObject.getString("type") ,
                    ownerObject.getBoolean("site_admin") );

            repository=new Repository(jsonObject.getInt("id"),
                    jsonObject.getString("node_id") ,
                    jsonObject.getString("name") ,
                    jsonObject.getString("full_name") ,
                    owner,
                    jsonObject.getBoolean("private") ,
                    jsonObject.getString("html_url") ,
                    jsonObject.get("description").toString() ,
                    jsonObject.getBoolean("fork") ,
                    jsonObject.getString("url") ,
                    jsonObject.getString("forks_url") ,
                    jsonObject.getString("keys_url") ,
                    jsonObject.getString("collaborators_url") ,
                    jsonObject.getString("teams_url") ,
                    jsonObject.getString("hooks_url") ,
                    jsonObject.getString("issue_events_url") ,
                    jsonObject.getString("events_url") ,
                    jsonObject.getString("assignees_url") ,
                    jsonObject.getString("branches_url") ,
                    jsonObject.getString("tags_url") ,
                    jsonObject.getString("blobs_url") ,
                    jsonObject.getString("git_tags_url") ,
                    jsonObject.getString("git_refs_url") ,
                    jsonObject.getString("trees_url") ,
                    jsonObject.getString("statuses_url") ,
                    jsonObject.getString("languages_url") ,
                    jsonObject.getString("stargazers_url") ,
                    jsonObject.getString("contributors_url") ,
                    jsonObject.getString("subscribers_url") ,
                    jsonObject.getString("subscription_url") ,
                    jsonObject.getString("commits_url") ,
                    jsonObject.getString("git_commits_url") ,
                    jsonObject.getString("comments_url") ,
                    jsonObject.getString("issue_comment_url") ,
                    jsonObject.getString("contents_url") ,
                    jsonObject.getString("compare_url") ,
                    jsonObject.getString("merges_url") ,
                    jsonObject.getString("archive_url") ,
                    jsonObject.getString("downloads_url") ,
                    jsonObject.getString("issues_url") ,
                    jsonObject.getString("pulls_url") ,
                    jsonObject.getString("milestones_url") ,
                    jsonObject.getString("notifications_url") ,
                    jsonObject.getString("labels_url") ,
                    jsonObject.getString("releases_url") ,
                    jsonObject.getString("deployments_url") ,
                    jsonObject.getString("created_at") ,
                    jsonObject.getString("updated_at") ,
                    jsonObject.getString("pushed_at") ,
                    jsonObject.getString("git_url") ,
                    jsonObject.getString("ssh_url") ,
                    jsonObject.getString("clone_url") ,
                    jsonObject.getString("svn_url") ,
                    jsonObject.get("homepage").toString() ,
                    jsonObject.getInt("size"),
                    jsonObject.getInt("stargazers_count"),
                    jsonObject.getInt("watchers_count"),
                    jsonObject.get("language").toString() ,
                    jsonObject.getBoolean("has_issues") ,
                    jsonObject.getBoolean("has_projects") ,
                    jsonObject.getBoolean("has_downloads") ,
                    jsonObject.getBoolean("has_wiki") ,
                    jsonObject.getBoolean("has_pages") ,
                    jsonObject.getInt("forks_count") ,
                    jsonObject.get("mirror_url").toString() ,
                    jsonObject.getBoolean("archived") ,
                    jsonObject.getInt("open_issues_count") ,
                    jsonObject.get("license").toString() ,
                    jsonObject.getInt("forks") ,
                    jsonObject.getInt("open_issues") ,
                    jsonObject.getInt("watchers") ,
                    jsonObject.getString("default_branch") );

            repositories.add(repository);
        }
        return repositories ;
    }

//    封装响应respenseContent-Json为List<Owner>
    public List<Owner> getFollowingOwnerObject(String responseContent){
        List<Owner> owners=new ArrayList<Owner>();
        JSONArray jsonArray=new JSONArray(responseContent);
        int length=jsonArray.length();

        for (int i=0;i<length;i++){
            Owner owner;
            JSONObject jsonObject=jsonArray.getJSONObject(i);
//            JSONObject ownerObject=jsonObject.getJSONObject("owner");
            owner=new Owner(jsonObject.getString("login") ,
                    jsonObject.getInt("id"),
                    jsonObject.getString("node_id") ,
                    jsonObject.getString("avatar_url") ,
                    jsonObject.getString("gravatar_id") ,
                    jsonObject.getString("url") ,
                    jsonObject.getString("html_url") ,
                    jsonObject.getString("followers_url") ,
                    jsonObject.getString("following_url") ,
                    jsonObject.getString("gists_url") ,
                    jsonObject.getString("starred_url") ,
                    jsonObject.getString("subscriptions_url") ,
                    jsonObject.getString("organizations_url") ,
                    jsonObject.getString("repos_url") ,
                    jsonObject.getString("events_url") ,
                    jsonObject.getString("received_events_url") ,
                    jsonObject.getString("type") ,
                    jsonObject.getBoolean("site_admin") );

            owners.add(owner);
        }
        return owners ;
    }

    //    获取git个人following数据
    public String getFollowing(String username){
        String content="";
        String url=URL_HEAD+username+"/following"+TOKEN;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        content=getString(url, httpclient);
        return content;
    }
}
