using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Gateways.RNGateways
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNGatewaysModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNGatewaysModule"/>.
        /// </summary>
        internal RNGatewaysModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNGateways";
            }
        }
    }
}
