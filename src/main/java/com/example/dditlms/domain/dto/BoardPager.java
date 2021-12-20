package com.example.dditlms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class BoardPager {
    private int totalcount; // 페이징에 적용할 전체 데이터 갯수
    private int pagenum; // 현재 페이지 번호
    private int contentnum; // 한페이지에 몇개 표시할지
    private int startPage=1; // 현재 페이지 블록의 시작 페이지
    private int endPage=5; // 현재 페이지 블럭의 마지막 페이지
    private boolean prev; // 이전 페이지로 가는 화살표
    private boolean next; //  다음 페이지로 가는 화살표
    private int currentblock; // 현재 페이지 블록
    private int lastblock; //  마지막 페이지 블록

    //이전, 다음 페이지 블록
    public void prevnext(int pagenum) {
        if(calcpage(totalcount,contentnum)<6){
            setPrev(false);
            setNext(false);
        }else if(pagenum>0 && pagenum<6){
            setPrev(false);
            setNext(true);
        }else if(getLastblock() == getCurrentblock()){
            setPrev(true);
            setNext(false);
        }else{
            setPrev(true);
            setNext(true);
        }
    }

    //전체 페이지 수를 계산하는 함수
    public int calcpage(int totalcount, int contentnum) {
        //12.5 -> 13page
        int totalpage = totalcount / contentnum;

        if(totalcount%contentnum > 0) {
            totalpage++;
        }

        return totalpage++;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public int getPagenum() {
        return pagenum;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public int getContentnum() {
        return contentnum;
    }

    public void setContentnum(int contentnum) {
        this.contentnum = contentnum;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int currentblock) {
        this.startPage = (currentblock*5)-4;
        /*
         *  1  // 1 2 3 4 5
         *  2  // 6 7 8 9 10
         *  3  // 11 12 13
         * */
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int getlastblock, int getcurrentblock) {
        // 마지막 페이지 블록을 구하는 곳
        if(getlastblock == getcurrentblock){
            this.endPage = calcpage(getTotalcount(),getContentnum()); // 전체페이지 개수가 오는곳
        }else{
            this.endPage = getStartPage()+4;
        }
    }

    public boolean isPrev() {
        return prev;
    }

    public void setPrev(boolean prev) {
        this.prev = prev;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }

    public int getCurrentblock() {
        return currentblock;
    }

    public void setCurrentblock(int pagenum) {
        //현재페이지 블록 구하는 곳  페이지 번호를 통해서 구한다.
        //페이지 번호 / 페이지 그룹안의 페이지 개수
        // 1p => 1 / 5 => 0.2   + 1 = 현재 페이지블록 1
        // 3p => 3 / 5 => 0.xx  + 1 = 현재 페이지 블록 1
        this.currentblock = pagenum/5;
        if(pagenum%5>0){
            this.currentblock++;
        }
    }

    public int getLastblock() {
        return lastblock;
    }

    public void setLastblock(int lastblock) {
        //10 , 5  = > 10 * 5 => 50
        // 12 5 / 50
        // 3
        this.lastblock = totalcount / (5*this.contentnum);

        if(totalcount %(5*this.contentnum)>0){
            this.lastblock++;
        }
    }
}
