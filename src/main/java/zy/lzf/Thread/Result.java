package zy.lzf.Thread;

/**
 * @program: Thread
 * @description: 结果类 保存查到的结果
 * @author: Joker
 * @create: 2023-07-17 13:52
 */
public class Result {
    private boolean found;

    private String path;

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
