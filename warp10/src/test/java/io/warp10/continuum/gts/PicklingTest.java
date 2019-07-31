//
//   Copyright 2019  SenX S.A.S.
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.
//

package io.warp10.continuum.gts;

import io.warp10.WarpConfig;
import io.warp10.script.MemoryWarpScriptStack;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.StringReader;

public class PicklingTest {

  static MemoryWarpScriptStack stack;

  @BeforeClass
  public static void beforeClass() throws Exception {
    StringBuilder props = new StringBuilder();

    props.append("warp.timeunits=us");
    WarpConfig.safeSetProperties(new StringReader(props.toString()));
    stack = new MemoryWarpScriptStack(null, null);
    stack.maxLimits();
  }

  @AfterClass
  public static void afterClass() throws Exception {
    stack.exec("DEPTH 0 == ASSERT");
  }

  @Test
  public void testPickledTS() throws Exception {

    stack.getSymbolTable().clear();
    stack.exec("[ 1 10 <% s %> FOR ] [] [] [] [ 1 10 <% DROP RAND %> FOR ] MAKEGTS" + System.lineSeparator());
    stack.exec("'gts' STORE" + System.lineSeparator());
    stack.exec("$gts 'gts' RENAME { 'k1' 'v1' 'k2' 'v2' } RELABEL { 'k3' 'v3' 'k4' 'v4' } SETATTRIBUTES" + System.lineSeparator());
    stack.exec("->PICKLE PICKLE-> 'map' STORE" + System.lineSeparator());

    //
    // Asserts
    //

    stack.exec("$map SIZE 5 == ASSERT" + System.lineSeparator());
    stack.exec("$map 'classname' GET 'gts' == ASSERT" + System.lineSeparator());
    stack.exec("$map 'labels' GET ->JSON $gts LABELS ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("$map 'attributes' GET ->JSON $gts ATTRIBUTES ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("$map 'timestamp' GET ->JSON $gts TICKS ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("$map 'value' GET ->JSON $gts VALUES ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("DEPTH 0 == ASSERT");
  }

  @Test
  public void testPickledGTS() throws Exception {

    stack.getSymbolTable().clear();
    stack.exec("[ 1 10 <% s %> FOR ] [ 1 10 <% us %> FOR ] [ 1 10 <% us %> FOR ] [] [ 1 10 <% DROP RAND %> FOR ] MAKEGTS" + System.lineSeparator());
    stack.exec("'gts' STORE" + System.lineSeparator());
    stack.exec("$gts 'gts' RENAME { 'k1' 'v1' 'k2' 'v2' } RELABEL { 'k3' 'v3' 'k4' 'v4' } SETATTRIBUTES" + System.lineSeparator());
    stack.exec("->PICKLE PICKLE-> 'map' STORE" + System.lineSeparator());

    //
    // Asserts
    //

    stack.exec("$map SIZE 7 == ASSERT" + System.lineSeparator());
    stack.exec("$map 'classname' GET 'gts' == ASSERT" + System.lineSeparator());
    stack.exec("$map 'labels' GET ->JSON $gts LABELS ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("$map 'attributes' GET ->JSON $gts ATTRIBUTES ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("$map 'timestamp' GET ->JSON $gts TICKS ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("$map 'value' GET ->JSON $gts VALUES ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("$map 'latitude' GET ->JSON $gts LOCATIONS DROP ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("$map 'longitude' GET ->JSON $gts LOCATIONS SWAP DROP ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("DEPTH 0 == ASSERT");
  }

  @Test
  public void testPickledGTSwithElevation() throws Exception {

    stack.getSymbolTable().clear();
    stack.exec("[ 1 10 <% s %> FOR ] [ 1 10 <% us %> FOR ] [ 1 10 <% us %> FOR ] [ 1 10 <% ms %> FOR ] [ 1 10 <% DROP RAND %> FOR ] MAKEGTS" + System.lineSeparator());
    stack.exec("'gts' STORE" + System.lineSeparator());
    stack.exec("$gts 'gts' RENAME { 'k1' 'v1' 'k2' 'v2' } RELABEL { 'k3' 'v3' 'k4' 'v4' } SETATTRIBUTES" + System.lineSeparator());
    stack.exec("->PICKLE PICKLE-> 'map' STORE" + System.lineSeparator());

    //
    // Asserts
    //

    stack.exec("$map SIZE 8 == ASSERT" + System.lineSeparator());
    stack.exec("$map 'classname' GET 'gts' == ASSERT" + System.lineSeparator());
    stack.exec("$map 'labels' GET ->JSON $gts LABELS ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("$map 'attributes' GET ->JSON $gts ATTRIBUTES ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("$map 'timestamp' GET ->JSON $gts TICKS ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("$map 'value' GET ->JSON $gts VALUES ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("$map 'latitude' GET ->JSON $gts LOCATIONS DROP ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("$map 'longitude' GET ->JSON $gts LOCATIONS SWAP DROP ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("$map 'elevation' GET ->JSON $gts ELEVATIONS ->JSON == ASSERT" + System.lineSeparator());
    stack.exec("DEPTH 0 == ASSERT");
  }
}
