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
    public int srcVert; // Индекс начальной вершины ребра
    public int destVert; // Индекс конечной вершины ребра
    public int distance; // Расстояние от начала до конца

    public User(int sv, int dv, int d) // Конструктор
    {
	srcVert = sv;
	destVert = dv;
	distance = d;
    }
}

// Класс чтоб помечать вершины
class Vertex {
    public String label; // Метка (например, 'A')
    public boolean isInTree;

    // -------------------------------------------------------------
    public Vertex(String lab) // Конструктор
    {
	label = lab;
	isInTree = false;
    }
    // -------------------------------------------------------------
} // Конец класса Vertex
// Массив отсортирован по убыванию от ячейки 0 до size-1

class PriorityQ {
    // Массив отсортирован по убыванию от ячейки 0 до size-1
    private final int SIZE = 20;
    private User[] queArray;
    private int size;

    // -------------------------------------------------------------
    public PriorityQ() // Конструктор
    {
	queArray = new User[SIZE];
	size = 0;
    }

    public void insert(User item) // Вставка элемента в порядке сортировки
    {
	int j;
	for (j = 0; j < size; j++)
	    // Поиск места для вставки
	    if (item.distance >= queArray[j].distance)
		break;
	for (int k = size - 1; k >= j; k--)
	    // Перемещение элементов вверх
	    queArray[k + 1] = queArray[k];
	queArray[j] = item; // Вставка элемента
	size++;
    }

    public User removeMin() // Извлечение наименьшего элемента
    {
	return queArray[--size];
    }

    public void removeN(int n) // Удаление элемента в позиции N
    {
	for (int j = n; j < size - 1; j++)
	    // Перемещение элементов вниз
	    queArray[j] = queArray[j + 1];
	size--;
    }

    public User peekMin() // Чтение наименьшего элемента
    {
	return queArray[size - 1];
    }

    public int size() // Получение количества элементов
    {
	return size;
    }

    public boolean isEmpty() // true, если очередь пуста
    {
	return (size == 0);
    }

    public User peekN(int n) // Чтение элемента в позиции N
    {
	return queArray[n];
    }

    public int find(int findDex) // Поиск элемента с заданным
    { // значением destVert
	for (int j = 0; j < size; j++)
	    if (queArray[j].destVert == findDex)
		return j;
	return -1;
    }
}

class Graph {
    private final int MAX_VERTS = 20;
    private final int INFINITY = 1000000;
    private Vertex vertexList[]; // Список вершин
    private int adjMat[][]; // Матрица смежности
    private int nVerts; // Текущее количество вершин
    private int currentVert;
    private PriorityQ thePQ;
    private int nTree; // Количество вершин в дереве
    static int sum = 0;

    // -------------------------------------------------------------

    public Graph() // Конструктор
    {
	vertexList = new Vertex[MAX_VERTS];
	// Матрица смежности
	adjMat = new int[MAX_VERTS][MAX_VERTS];
	nVerts = 0;
	for (int j = 0; j < MAX_VERTS; j++)
	    // Матрица смежности
	    for (int k = 0; k < MAX_VERTS; k++)
		// заполняется нулями
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
    public void mstw() // Построение минимального остовного дерева
    {
	currentVert = 0; // Начиная с ячейки 0
	while (nTree < nVerts - 1) // Пока не все вершины включены в дерево
	{ // Включение currentVert в дерево
	    vertexList[currentVert].isInTree = true;
	    nTree++;
	    // Вставка в приоритетную очередь ребер, смежных с currentVert
	    for (int j = 0; j < nVerts; j++) // Для каждой вершины
	    {
		if (j == currentVert) // Пропустить, если текущая вершина

		    continue;
		if (vertexList[j].isInTree) // Пропустить, если уже в дереве
		    continue;
		int distance = adjMat[currentVert][j];
		if (distance == INFINITY) // Пропустить, если нет ребер
		    continue;
		putInPQ(j, distance); // Поместить в приоритетную очередь
	    }
	    if (thePQ.size() == 0) // Очередь не содержит вершин?
	    {
		System.out.println(" GRAPH NOT CONNECTED");
		return;
	    }
	    // Удаление ребра с минимальным расстоянием из очереди
	    User theEdge = thePQ.removeMin();
	    int sourceVert = theEdge.srcVert;
	    currentVert = theEdge.destVert;
	    // Вывод ребра от начальной до текущей вершины
	    System.out.print(vertexList[sourceVert].label);
	    System.out.print(vertexList[currentVert].label);
	    System.out.print(" ");
	    sum = sum + adjMat[sourceVert][currentVert];

	}
	// Минимальное остовное дерево построено
	for (int j = 0; j < nVerts; j++) {
	    // Снятие пометки с вершин
	    vertexList[j].isInTree = false;
	}
	System.out.println(sum);
    }

    // -------------------------------------------------------------
    public void putInPQ(int newVert, int newDist) {
	// Существует ли другое ребро с той же конечной вершиной?
	int queueIndex = thePQ.find(newVert); // Получение индекса
	if (queueIndex != -1) // Если ребро существует,
	{ // получить его
	    User tempEdge = thePQ.peekN(queueIndex);
	    int oldDist = tempEdge.distance;
	    if (oldDist > newDist) // Если новое ребро короче,
	    {
		thePQ.removeN(queueIndex); // удалить старое ребро
		User theEdge = new User(currentVert, newVert, newDist);
		thePQ.insert(theEdge); // Вставка нового ребра
	    }
	    // Иначе ничего не делается; оставляем старую вершину
	} else // Ребра с той же конечной вершиной не существует
	{ // Вставка нового ребра
	    User theEdge = new User(currentVert, newVert, newDist);
	    thePQ.insert(theEdge);
	}
    }
    // -------------------------------------------------------------
} // Конец класса Graph
// //////////////////////////////////////////////////////////////
