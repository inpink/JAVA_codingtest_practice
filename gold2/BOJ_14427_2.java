package MainPack; //자바는 제출 시 package 쓰면 안 됨

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Main { // class name은 Main 이어야 함

	static void init(int[] a, int[] tree, int node, int start, int end) { 
		if (start==end) tree[node]=start; //index를 담음 
		else {
			init(a,tree,2*node,start,(start+end)/2);
			init(a,tree,2*node+1,(start+end)/2+1,end);
			
			//index가 담겨있기에, 그 index를 이용하여 a에서 값을 꺼내서 최솟값 비교. 담는 것은 다시 index여야 함.
			if (a[tree[node*2]]<=a[tree[node*2+1]]) tree[node]=tree[node*2];
			else tree[node]=tree[node*2+1];
		}
	}
	
	static int query(int[] a, int[] tree, int node, int start, int end, int left, int right) { 
		if (left>end || right<start) return -1; //나올 수 없는 값을 이용함
		if (left<=start && end<=right ) return tree[node]; //index 리턴
		
		int lmin=query(a,tree, node*2, start, (start+end)/2, left,right);
		int rmin=query(a, tree,node*2+1,(start+end)/2+1,end,left,right);
		
		//마찬가지로 index를 이용하여 a에서 값을 꺼내서 최솟값 비교. 담는 것은 다시 index여야 함.
		//둘 중에 -1이 있으면 그쪽에는 index와 관련없는 곳이라는 뜻이기 떄문에 상대를 위로 보냄
		if (lmin==-1) return rmin;
		else if (rmin==-1) return lmin;
		else if (a[lmin]<=a[rmin]) return lmin; 
		else return rmin;
	}
	
	static void update(int[] a, int[] tree, int node, int start,int end, int index, int val ) {
		if (index<start || index>end) return;
		if (start==end && start==index) { //말단 노드이자, 내가 업데이트 시키고자 하는 index번째
			a[start]=val;
			//tree[node]=val; //index가 담기기에 tree의 말단 노드 값은 바뀌지 않는다!
			return;
		}
		//자식들 호출하는 재귀
		update(a,tree,node*2,start,(start+end)/2,index,val);
		update(a,tree,node*2+1,(start+end)/2+1,end,index,val);
		
		//대신, 거슬러 올라가며 min index를 다시 업데이트 해줘야 함
		if (a[tree[node*2]]<=a[tree[node*2+1]]) tree[node]=tree[node*2];
		else tree[node]=tree[node*2+1];
	}
	
	public static void main(String[] args) throws IOException { // 예외처리 꼭 해줘야 함
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
	        
			int n= Integer.parseInt(br.readLine());
			int a[]=new int[n]; //a리스트는 0~n-1번  //10억이라는 숫자가 10만번 합쳐지면 10^14로 long 범위 써야 함 
			
			StringTokenizer st= new StringTokenizer(br.readLine());
			for (int i=0; i<n; i++) a[i]=Integer.parseInt(st.nextToken());
			
			int h=(int)Math.ceil(Math.log(n)/Math.log(2)); 
			int tree[]=new int[1<<(h+1)]; 
			init(a,tree,1,0,n-1);	
			
			int m= Integer.parseInt(br.readLine());
			for (int i=0; i<m;i++) {
				StringTokenizer st1=new StringTokenizer(br.readLine());
				if (st1.nextToken().equals("1")) update(a,tree,1,0,n-1,Integer.parseInt(st1.nextToken())-1,Integer.parseInt(st1.nextToken()));
				else bw.write(tree[1]+1+"\n");
			}
			bw.flush(); bw.close();
	   }
}
