pragma solidity >=0.4.22 <0.6.0;
pragma experimental ABIEncoderV2;

import "./WeCrossHub.sol";
import "./LibString.sol";

contract CRUDInterchain {
    WeCrossHub hub;

    mapping(string => string) internal stub;


    function init(address _hub) public
    {
        hub = WeCrossHub(_hub);
    }

    function interchain(string memory _path, string memory _method, string memory _args, string memory _callbackPath, string memory _callbackMethod) public
    returns(string memory)
    {
        string[] memory args = new string[](1);
        args[0] = _args;

        return hub.interchainInvoke(_path, _method, args, _callbackPath, _callbackMethod);
    }

    function callback(bool state, string[] memory _result) public
    returns(string[] memory)
    {
        // if(state) {
        //     data = _result;
        // }
        return _result;
    }
    
    function interchainGet(string memory key) public 
    returns(string memory)
    {
        string memory key1 = LibString.substrByCharIndex(key,2,LibString.lenOfChars(key)-4);
        return stub[key1];
    }
    
    function get(string memory key) public view
    returns(string memory)
    {
        return stub[key];
    }

    function set(string memory key,string memory value) public
    returns(string memory)
    {
        stub[key] = value;
        return stub[key];
    }

}
