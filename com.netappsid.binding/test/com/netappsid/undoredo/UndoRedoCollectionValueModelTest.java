package com.netappsid.undoredo;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.google.common.collect.ImmutableList;
import com.netappsid.binding.beans.CollectionValueModel;
import com.netappsid.observable.CollectionChangeEvent;
import com.netappsid.observable.CollectionChangeListener;
import com.netappsid.observable.ListDifference;
import com.netappsid.observable.ObservableCollections;
import com.netappsid.observable.ObservableList;

public class UndoRedoCollectionValueModelTest
{
	private UndoRedoManager undoRedoManager;
	private CollectionValueModel collectionValueModel;
	private CollectionChangeListener listener;
	private UndoRedoCollectionValueModel undoRedoCollectionValueModel;
	private CollectionChangeListener undoRedoManagerPushHandler;
	private Object oldObject;
	private Object newObject;
	private ListDifference difference;
	private Object firstObject;


	@Before
	public void setUp()
	{
		undoRedoManager = mock(UndoRedoManager.class);

		firstObject = new Object();
		ObservableList<Object> newObservableArrayList = ObservableCollections.newObservableArrayList(firstObject);

		collectionValueModel = mock(CollectionValueModel.class);
		when(collectionValueModel.getValue()).thenReturn(newObservableArrayList);
		listener = mock(CollectionChangeListener.class);

		undoRedoCollectionValueModel = new UndoRedoCollectionValueModel(undoRedoManager, collectionValueModel);

		oldObject = new Object();
		newObject = new Object();

		difference = new ListDifference(ImmutableList.of(oldObject), ImmutableList.of(newObject));
	}

	@Test
	public void testAddCollectionChangeListener()
	{
		undoRedoCollectionValueModel.addCollectionChangeListener(listener);

		verify(collectionValueModel).addCollectionChangeListener(listener);
	}

	@Test
	public void testRemoveCollectionChangeListener()
	{
		undoRedoCollectionValueModel.removeCollectionChangeListener(listener);

		verify(collectionValueModel).removeCollectionChangeListener(listener);
	}

	@Test
	public void testUndo()
	{
		ObservableList<Object> newObservableArrayList = ObservableCollections.newObservableArrayList(newObject);
		when(collectionValueModel.getValue()).thenReturn(newObservableArrayList);
		undoRedoCollectionValueModel.undo(new CollectionChangeEvent(newObservableArrayList, difference));

		InOrder inOrder = inOrder(collectionValueModel, undoRedoManager, collectionValueModel);
		inOrder.verify(collectionValueModel).removeCollectionChangeListener(undoRedoCollectionValueModel.getUndoRedoManagerPushHandler());
		inOrder.verify(undoRedoManager, never()).push(any(CollectionChangeOperation.class));
		inOrder.verify(collectionValueModel).addCollectionChangeListener(undoRedoCollectionValueModel.getUndoRedoManagerPushHandler());
		
		assertTrue(newObservableArrayList.contains(oldObject));
		assertFalse(newObservableArrayList.contains(newObject));
	}

	@Test
	public void testRedo()
	{
		ObservableList<Object> newObservableArrayList = ObservableCollections.newObservableArrayList(oldObject);
		when(collectionValueModel.getValue()).thenReturn(newObservableArrayList);
		undoRedoCollectionValueModel.redo(new CollectionChangeEvent(newObservableArrayList, difference));

		InOrder inOrder = inOrder(collectionValueModel, undoRedoManager, collectionValueModel);
		inOrder.verify(collectionValueModel).removeCollectionChangeListener(undoRedoCollectionValueModel.getUndoRedoManagerPushHandler());
		inOrder.verify(undoRedoManager, never()).push(any(CollectionChangeOperation.class));
		inOrder.verify(collectionValueModel).addCollectionChangeListener(undoRedoCollectionValueModel.getUndoRedoManagerPushHandler());

		assertFalse(newObservableArrayList.contains(oldObject));
		assertTrue(newObservableArrayList.contains(newObject));
	}

	@Test
	public void testSize()
	{
		undoRedoCollectionValueModel.size();
		verify(collectionValueModel).size();
	}

	@Test
	public void testAdd()
	{
		Object added = new Object();
		undoRedoCollectionValueModel.add(added);
		verify(collectionValueModel).add(added);
	}

	@Test
	public void testAddAll()
	{
		List added = Arrays.asList(new Object(), new Object());
		undoRedoCollectionValueModel.addAll(added);
		verify(collectionValueModel).addAll(added);
	}

	@Test
	public void testClear()
	{
		undoRedoCollectionValueModel.clear();
		verify(collectionValueModel).clear();
	}

	@Test
	public void testContains()
	{
		undoRedoCollectionValueModel.contains(firstObject);
		verify(collectionValueModel).contains(firstObject);
	}

	@Test
	public void testContainsAll()
	{
		List added = Arrays.asList(new Object(), new Object());
		undoRedoCollectionValueModel.containsAll(added);
		verify(collectionValueModel).containsAll(added);
	}

	@Test
	public void testIsEmpty()
	{
		undoRedoCollectionValueModel.isEmpty();
		verify(collectionValueModel).isEmpty();
	}

	@Test
	public void testIterator()
	{
		undoRedoCollectionValueModel.iterator();
		verify(collectionValueModel).iterator();
	}

	@Test
	public void testRemove()
	{
		undoRedoCollectionValueModel.remove(firstObject);
		verify(collectionValueModel).remove(firstObject);
	}

	@Test
	public void testRemoveAll()
	{
		List removed = Arrays.asList(new Object(), new Object());
		undoRedoCollectionValueModel.removeAll(removed);
		verify(collectionValueModel).removeAll(removed);
	}

	@Test
	public void testRetainAll()
	{
		List retained = Arrays.asList(new Object(), new Object());
		undoRedoCollectionValueModel.retainAll(retained);
		verify(collectionValueModel).retainAll(retained);
	}
}
