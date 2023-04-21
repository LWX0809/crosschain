
// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.8.0;

pragma experimental ABIEncoderV2;

//import "./LWX_Storage.sol";
import "./LibString.sol";
import "./LWX_CrossChainContractAdditional.sol";

contract CrossChainContractTest is LWX_Storage {
    //合约是否被初始化
    bool private _Init;
    uint256 public value1;

    constructor() public{

    }
    function createCrossChainWrite(string memory  funcn,string memory key,uint8 value) public {
        value1 = value;
        emit startTxEvent("createCrossChainWrite", key, value);
    }
    function createCrossChainRead(string memory funcn,string memory key) public {
        emit startTxEvent("createCrossChainRead", key, 100);
    }
}
