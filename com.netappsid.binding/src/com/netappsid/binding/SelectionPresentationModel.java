package com.netappsid.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import com.netappsid.binding.selection.SelectionHolder;
import com.netappsid.binding.selection.SelectionModel;
import com.netappsid.validate.Validate;

/**
 * 
 * 
 * @author Eric Belanger
 * @author NetAppsID Inc.
 * @version $Revision: 1.3 $
 */
@SuppressWarnings("serial")
public class SelectionPresentationModel extends PresentationModel
{
	public static final String DEFAULT_SELECTION = "selected";

	public static final String PROPERTYNAME_BEAN_LIST = "beanList";
	private ValueModel beanListChannel;

	private Map<String, SelectionModel> selectionModels;

	public SelectionPresentationModel(Class<?> beanClass)
	{
		this(beanClass, new ValueHolder(null, true));
	}

	public SelectionPresentationModel(Class<?> beanClass, List<?> beanList)
	{
		this(beanClass, new ValueHolder(beanList, true));
	}

	public SelectionPresentationModel(Class<?> beanClass, ValueModel beanListChannel)
	{
		this.beanListChannel = beanListChannel;

		setBeanClass(beanClass);
	}

	public void addBeanPropertyChangeListener(PropertyChangeListener listener)
	{
		getSubModel(DEFAULT_SELECTION).addBeanPropertyChangeListener(listener);
	}

	public void addBeanPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		int index = propertyName.indexOf('.');

		if (index != -1)
		{
			getSubModel(propertyName.substring(0, index)).addBeanPropertyChangeListener(propertyName.substring(index + 1, propertyName.length()), listener);
		}
		else
		{
			throw new IllegalArgumentException("Property name must start with a selection key.");
		}
	}

	public Object getBean()
	{
		return getSelectedBean(DEFAULT_SELECTION);
	}

	public ValueModel getBeanChannel()
	{
		return getSelectedBeanChannel(DEFAULT_SELECTION);
	}

	public List<?> getBeanList()
	{
		return (List<?>) getBeanListChannel().getValue();
	}

	public ValueModel getBeanListChannel()
	{
		return beanListChannel;
	}

	public PropertyChangeListener[] getBeanPropertyChangeListeners()
	{
		return getSelectedBeanPropertyChangeListeners(DEFAULT_SELECTION);
	}

	public PropertyChangeListener[] getBeanPropertyChangeListeners(String propertyName)
	{
		int index = propertyName.indexOf('.');

		if (index != -1)
		{
			return getSubModel(propertyName.substring(0, index)).getBeanPropertyChangeListeners(propertyName.substring(index + 1, propertyName.length()));
		}
		else
		{
			throw new IllegalArgumentException("Property name must start with a selection key.");
		}
	}

	public Object getSelectedBean(String selectionKey)
	{
		return getSubModel(selectionKey).getBean();
	}

	public ValueModel getSelectedBeanChannel(String selectionKey)
	{
		return getSubModel(selectionKey).getBeanChannel();
	}

	public PropertyChangeListener[] getSelectedBeanPropertyChangeListeners(String selectionKey)
	{
		return getSubModel(selectionKey).getBeanPropertyChangeListeners();
	}

	public SelectionModel getSelectionModel()
	{
		return getSelectionModel(DEFAULT_SELECTION);
	}

	public SelectionModel getSelectionModel(String selectionKey)
	{
		SelectionModel selectionModel = getSelectionModels().get(selectionKey);

		if (selectionModel == null)
		{
			selectionModel = new SelectionHolder();
			selectionModel.addSelectionChangeListener(new SelectionChangeHandler(selectionKey));
			getSelectionModels().put(selectionKey, selectionModel);
		}

		return selectionModel;
	}

	public PresentationModel getSubModel(String propertyName)
	{
		PresentationModel subModel = null;
		int index = propertyName.indexOf('.');

		if (index == -1)
		{
			subModel = getSubModels().get(propertyName);

			if (subModel == null)
			{
				subModel = PresentationModelFactory.createPresentationModel(this);
				getSubModels().put(propertyName, subModel);
			}
		}
		else
		{
			subModel = getSubModels().get(propertyName.substring(0, index));

			if (subModel == null)
			{
				subModel = PresentationModelFactory.createPresentationModel(this, propertyName.substring(0, index));
				getSubModels().put(propertyName.substring(0, index), subModel);
			}

			subModel = subModel.getSubModel(propertyName.substring(index + 1, propertyName.length()));
		}

		return subModel;
	}

	public Object getValue(String propertyName)
	{
		return getValueModel(propertyName).getValue();
	}

	public ValueModel getValueModel(String propertyName)
	{
		ValueModel valueModel = null;
		int index = propertyName.lastIndexOf('.');

		if (index != -1)
		{
			valueModel = getSubModel(propertyName.substring(0, index)).getValueModel(propertyName.substring(index + 1, propertyName.length()));
		}
		else
		{
			throw new IllegalArgumentException("Property name must start with a selection key.");
		}

		return valueModel;
	}

	public ValueModel getValueModel(String propertyName, String getterName, String setterName)
	{
		ValueModel valueModel = null;
		int index = propertyName.lastIndexOf('.');

		if (index != -1)
		{
			valueModel = getSubModel(propertyName.substring(0, index)).getValueModel(propertyName.substring(index + 1, propertyName.length()), getterName,
					setterName);
		}
		else
		{
			throw new IllegalArgumentException("Property name must start with a selection key.");
		}

		return valueModel;
	}

	public void releaseBeanListeners()
	{
		releaseSelectedBeanListeners(DEFAULT_SELECTION);
	}

	public void releaseSelectedBeanListeners(String selectionKey)
	{
		getSubModel(selectionKey).releaseBeanListeners();
	}

	public void removeBeanPropertyChangeListener(PropertyChangeListener listener)
	{
		removeSelectedBeanPropertyChangeListener(DEFAULT_SELECTION, listener);
	}

	public void removeBeanPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{
		int index = propertyName.indexOf('.');

		if (index != -1)
		{
			getSubModel(propertyName.substring(0, index)).removeBeanPropertyChangeListener(propertyName.substring(index + 1, propertyName.length()), listener);
		}
		else
		{
			throw new IllegalArgumentException("Property name must start with a selection key.");
		}
	}

	public void removeSelectedBeanPropertyChangeListener(String selectionKey, PropertyChangeListener listener)
	{
		getSubModel(selectionKey).removeBeanPropertyChangeListener(listener);
	}

	public void setBean(Object newBean)
	{
		setSelectedBean(DEFAULT_SELECTION, newBean);
	}

	public void setBeanList(List<?> beanList)
	{
		beanListChannel.setValue(beanList);
	}

	public void setSelectedBean(String selectionKey, Object newBean)
	{
		getSubModel(selectionKey).setBean(newBean);
	}

	public void setValue(String propertyName, Object newValue)
	{
		getValueModel(propertyName).setValue(newValue);
	}

	private Map<String, SelectionModel> getSelectionModels()
	{
		if (selectionModels == null)
		{
			selectionModels = new HashMap<String, SelectionModel>();
		}

		return selectionModels;
	}

	private final class SelectionChangeHandler implements PropertyChangeListener
	{
		private String selectionKey;

		public SelectionChangeHandler(String selectionKey)
		{
			setSelectionKey(selectionKey);
		}

		@SuppressWarnings("unchecked")
		public void propertyChange(PropertyChangeEvent evt)
		{
			SortedSet<Integer> indexes = (SortedSet<Integer>) evt.getNewValue();

			if (indexes != null && indexes.size() == 1 && getBeanList() != null && getBeanList().size() > indexes.first())
			{
				setSelectedBean(selectionKey, getBeanList().get(indexes.first()));
			}
			else
			{
				setSelectedBean(selectionKey, null);
			}
		}

		public void setSelectionKey(String selectionKey)
		{
			this.selectionKey = Validate.notNull(selectionKey, "Selection key must not be null.");
		}
	}
}