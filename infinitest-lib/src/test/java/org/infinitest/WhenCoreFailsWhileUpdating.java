/*
 * This file is part of Infinitest.
 *
 * Copyright (C) 2010
 * "Ben Rady" <benrady@gmail.com>,
 * "Rod Coffin" <rfciii@gmail.com>,
 * "Ryan Breidenbach" <ryan.breidenbach@gmail.com>, et al.
 *
 * Infinitest is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Infinitest is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Infinitest.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.infinitest;

import static org.easymock.EasyMock.*;
import static org.infinitest.CoreDependencySupport.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.infinitest.changedetect.ChangeDetector;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

public class WhenCoreFailsWhileUpdating
{
    Mockery context = new Mockery();
    private InfinitestCore core;
    private StubTestDetector testDetector;
    private ChangeDetector changeDetector;

    @Before
    public void inContext() throws IOException
    {
        changeDetector = createMock(ChangeDetector.class);
        expect(changeDetector.filesWereRemoved()).andReturn(false);
        expect(changeDetector.findChangedFiles()).andThrow(new IOException());
        changeDetector.clear();
        replay(changeDetector);
        testDetector = new StubTestDetector();
        core = createCore(changeDetector, testDetector);
    }

    @Test
    public void shouldIgnoreFailureAndReloadIndex()
    {
        core.update();
        assertTrue(testDetector.isCleared());
        verify(changeDetector);
    }
}
