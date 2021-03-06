/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the license at usr/src/OPENSOLARIS.LICENSE
 * or http://www.opensolaris.org/os/licensing.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at usr/src/OPENSOLARIS.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 */

/*
 * Copyright (c) 2010, 2012, Oracle and/or its affiliates. All rights reserved.
 */

package com.oracle.solaris.vp.panel.swing.view;

import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.event.*;
import com.oracle.solaris.vp.panel.common.action.StructuredAction;
import com.oracle.solaris.vp.panel.common.model.ManagedObject;
import com.oracle.solaris.vp.util.common.propinfo.PropInfo;
import com.oracle.solaris.vp.util.misc.finder.Finder;
import com.oracle.solaris.vp.util.swing.ExtScrollPane;

/**
 * The {@code TreeTableObjectsVeiw} class displays the ancestors of a {@code
 * ManagedObject} in a {@code TreeTable}.
 */
public class TreeTableObjectsView extends ObjectsView<ManagedObject> {
    //
    // Instance data
    //

    private ManagedObjectTreeTable table;
    private ManagedObjectTreeTableModel model;
    private ExtScrollPane scroll;

    //
    // Constructors
    //

    @SuppressWarnings({"unchecked"})
    public TreeTableObjectsView(ManagedObject<?> pObject,
	StructuredAction<List<ManagedObject>, ?, ?>[] mActions,
	StructuredAction<List<ManagedObject>, ?, ?> dmAction,
	PropInfo<ManagedObject, ?>[] props) {

	super((ManagedObject<ManagedObject>)pObject, mActions, dmAction);

	SimplePopupMenu popupMenu = mActions == null || mActions.length == 0 ?
	    null : new SimplePopupMenu(mActions);

	model = new ManagedObjectTreeTableModel(pObject, props);

	model.addTreeModelListener(
	    new TreeModelListener() {
		@Override
		public void treeNodesChanged(TreeModelEvent e) {
		    fireObjectCountChange();
		}

		@Override
		public void treeNodesInserted(TreeModelEvent e) {
		    fireObjectCountChange();
		}

		@Override
		public void treeNodesRemoved(TreeModelEvent e) {
		    fireObjectCountChange();
		}

		@Override
		public void treeStructureChanged(TreeModelEvent e) {
		    fireObjectCountChange();
		}
	    });

	table = new ManagedObjectTreeTable(model, popupMenu, dmAction);
	table.setRootVisible(false);

	// Notify Actions of changes in selection model
	table.getSelectionModel().addListSelectionListener(
	    updateActionSelectionListener);

	scroll = new ExtScrollPane(table);
	scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    }

    //
    // ObjectsView methods
    //

    @Override
    public ExtScrollPane getComponent() {
	return scroll;
    }

    @Override
    public String getName() {
	return Finder.getString("objects.header.view.value.table");
    }

    @Override
    public int getObjectCount() {
	return table.getRowCount();
    }

    @Override
    public List<ManagedObject> getSelection() {
	return table.getSelectedObjects();
    }

    //
    // TreeTableObjectsView methods
    //

    public ManagedObjectTreeTable getTable() {
	return table;
    }
}
