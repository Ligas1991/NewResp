package ua.i.pustovalov.taskTwo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MyTest {
    public static void main(String[] args) {

    }
}

class User {
    public int srcVert; // ������ ��������� ������� �����
    public int destVert; // ������ �������� ������� �����
    public int distance; // ���������� �� ������ �� �����

    public User(int sv, int dv, int d) // �����������
    {
	srcVert = sv;
	destVert = dv;
	distance = d;
    }
}

// ����� ���� �������� �������
class Vertex {
    public String label; // ����� (��������, 'A')
    public boolean isInTree;

    // -------------------------------------------------------------
    public Vertex(String lab) // �����������
    {
	label = lab;
	isInTree = false;
    }
    // -------------------------------------------------------------
} // ����� ������ Vertex
// ������ ������������ �� �������� �� ������ 0 �� size-1

class PriorityQ {
    // ������ ������������ �� �������� �� ������ 0 �� size-1
    private final int SIZE = 20;
    private User[] queArray;
    private int size;

    // -------------------------------------------------------------
    public PriorityQ() // �����������
    {
	queArray = new User[SIZE];
	size = 0;
    }

    public void insert(User item) // ������� �������� � ������� ����������
    {
	int j;
	for (j = 0; j < size; j++)
	    // ����� ����� ��� �������
	    if (item.distance >= queArray[j].distance)
		break;
	for (int k = size - 1; k >= j; k--)
	    // ����������� ��������� �����
	    queArray[k + 1] = queArray[k];
	queArray[j] = item; // ������� ��������
	size++;
    }

    public User removeMin() // ���������� ����������� ��������
    {
	return queArray[--size];
    }

    public void removeN(int n) // �������� �������� � ������� N
    {
	for (int j = n; j < size - 1; j++)
	    // ����������� ��������� ����
	    queArray[j] = queArray[j + 1];
	size--;
    }

    public User peekMin() // ������ ����������� ��������
    {
	return queArray[size - 1];
    }

    public int size() // ��������� ���������� ���������
    {
	return size;
    }

    public boolean isEmpty() // true, ���� ������� �����
    {
	return (size == 0);
    }

    public User peekN(int n) // ������ �������� � ������� N
    {
	return queArray[n];
    }

    public int find(int findDex) // ����� �������� � ��������
    { // ��������� destVert
	for (int j = 0; j < size; j++)
	    if (queArray[j].destVert == findDex)
		return j;
	return -1;
    }
}

class Graph {
    private final int MAX_VERTS = 20;
    private final int INFINITY = 1000000;
    private Vertex vertexList[]; // ������ ������
    private int adjMat[][]; // ������� ���������
    private int nVerts; // ������� ���������� ������
    private int currentVert;
    private PriorityQ thePQ;
    private int nTree; // ���������� ������ � ������
    static int sum = 0;

    // -------------------------------------------------------------

    public Graph() // �����������
    {
	vertexList = new Vertex[MAX_VERTS];
	// ������� ���������
	adjMat = new int[MAX_VERTS][MAX_VERTS];
	nVerts = 0;
	for (int j = 0; j < MAX_VERTS; j++)
	    // ������� ���������
	    for (int k = 0; k < MAX_VERTS; k++)
		// ����������� ������
		adjMat[j][k] = INFINITY;
	thePQ = new PriorityQ();
    }

    // -------------------------------------------------------------
    public void addVertex(String lab) {
	vertexList[nVerts++] = new Vertex(lab);
    }

    // -------------------------------------------------------------
    public void addEdge(int start, int end, int weight) {
	adjMat[start][end] = weight;
	adjMat[end][start] = weight;
    }

    // -------------------------------------------------------------
    public void displayVertex(int v) {
	System.out.print(vertexList[v].label);
    }

    // -------------------------------------------------------------
    public void mstw() // ���������� ������������ ��������� ������
    {
	currentVert = 0; // ������� � ������ 0
	while (nTree < nVerts - 1) // ���� �� ��� ������� �������� � ������
	{ // ��������� currentVert � ������
	    vertexList[currentVert].isInTree = true;
	    nTree++;
	    // ������� � ������������ ������� �����, ������� � currentVert
	    for (int j = 0; j < nVerts; j++) // ��� ������ �������
	    {
		if (j == currentVert) // ����������, ���� ������� �������

		    continue;
		if (vertexList[j].isInTree) // ����������, ���� ��� � ������
		    continue;
		int distance = adjMat[currentVert][j];
		if (distance == INFINITY) // ����������, ���� ��� �����
		    continue;
		putInPQ(j, distance); // ��������� � ������������ �������
	    }
	    if (thePQ.size() == 0) // ������� �� �������� ������?
	    {
		System.out.println(" GRAPH NOT CONNECTED");
		return;
	    }
	    // �������� ����� � ����������� ����������� �� �������
	    User theEdge = thePQ.removeMin();
	    int sourceVert = theEdge.srcVert;
	    currentVert = theEdge.destVert;
	    // ����� ����� �� ��������� �� ������� �������
	    System.out.print(vertexList[sourceVert].label);
	    System.out.print(vertexList[currentVert].label);
	    System.out.print(" ");
	    sum = sum + adjMat[sourceVert][currentVert];

	}
	// ����������� �������� ������ ���������
	for (int j = 0; j < nVerts; j++) {
	    // ������ ������� � ������
	    vertexList[j].isInTree = false;
	}
	System.out.println(sum);
    }

    // -------------------------------------------------------------
    public void putInPQ(int newVert, int newDist) {
	// ���������� �� ������ ����� � ��� �� �������� ��������?
	int queueIndex = thePQ.find(newVert); // ��������� �������
	if (queueIndex != -1) // ���� ����� ����������,
	{ // �������� ���
	    User tempEdge = thePQ.peekN(queueIndex);
	    int oldDist = tempEdge.distance;
	    if (oldDist > newDist) // ���� ����� ����� ������,
	    {
		thePQ.removeN(queueIndex); // ������� ������ �����
		User theEdge = new User(currentVert, newVert, newDist);
		thePQ.insert(theEdge); // ������� ������ �����
	    }
	    // ����� ������ �� ��������; ��������� ������ �������
	} else // ����� � ��� �� �������� �������� �� ����������
	{ // ������� ������ �����
	    User theEdge = new User(currentVert, newVert, newDist);
	    thePQ.insert(theEdge);
	}
    }
    // -------------------------------------------------------------
} // ����� ������ Graph
// //////////////////////////////////////////////////////////////
