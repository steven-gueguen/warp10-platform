//
//   Copyright 2020  SenX S.A.S.
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

package io.warp10.script.ext.stackps;

import io.warp10.script.NamedWarpScriptFunction;
import io.warp10.script.WarpScriptException;
import io.warp10.script.WarpScriptStack;
import io.warp10.script.WarpScriptStackFunction;

public class STACKPSSECRET extends NamedWarpScriptFunction implements WarpScriptStackFunction {
  public STACKPSSECRET(String name) {
    super(name);
  }

  @Override
  public Object apply(WarpScriptStack stack) throws WarpScriptException {
    
    String secret = StackPSWarpScriptExtension.STACKPS_SECRET;
    
    if (null == secret) {
      throw new WarpScriptException(getName() + " cannot be used if a stackps secret was not configured.");
    }
    
    Object top = stack.pop();
    
    if (!secret.equals(top)) {
      throw new WarpScriptException(getName() + " invalid secret.");
    }
    
    // pop the new secret to use
    top = stack.pop();
    
    if (!(top instanceof String)) {
      throw new WarpScriptException(getName() + " expects a new secret which is a non null string.");
    }
    
    StackPSWarpScriptExtension.STACKPS_SECRET = String.valueOf(top);
    
    return stack;
  }
}
