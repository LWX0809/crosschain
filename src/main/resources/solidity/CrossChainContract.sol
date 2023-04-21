// SPDX-License-Identifier: MIT
pragma solidity >=0.4.22 <0.8.0;

pragma experimental ABIEncoderV2;

import "./LibString.sol";
import "./CrossChainContractAdditional.sol";

/// @title Cross-chain main contract
contract CrossChainContract is Storage {
    bool private _Init;

    /// @notice Cross-chain contract initialization. Can be executed once.
    function initialize(address crossChainAdd) public initializer {
        _Owner = msg.sender;
        _Version = "V1";
        _MainChainCode = 0;
        _CrossChainContractAdditional = CrossChainContractAdditional(
            crossChainAdd
        );
        _Init = true;
    }

    //修改器
    modifier onlyOwner() {
        require(
            msg.sender == _Owner,
            "This function is restricted to the contract's owner"
        );
        _;
    }

    modifier initializer() {
        require(_Init == false, "The contract has been initialized");
        _;
    }

    /// @notice Set subchain AC code. Can only be executed by the contract owner.
    //设置子链 AC 码。 只能由合约所有者执行   ???参数有什么用
    function setChainCode(string memory chainCode) public onlyOwner {
        delegateAdditional();
    }

    /// @notice Set subchain cross-chain gateway. Can only be executed by the contract owner.
    //设置子链跨链网关。 只能由合同所有者执行。
    function setGateway(address gatewayAddress) public onlyOwner {
        delegateAdditional();
    }

    /// @notice Generate cross-chain transaction number
    /// @notice srcChainCode + ':' + destChainCode + ':' + keccak256(msg.sender, _CrossTxNum, "1").substr(0, 32).
    function createCrossTxNo(
        string memory srcChainCode,
        string memory destChainCode
    ) internal returns (string memory) {
        bytes32 data = keccak256(
            //  abi.encodePacked 在没有填充的情况下进行编码
            abi.encodePacked(msg.sender, _CrossTxNum, "1")
        );
        _CrossTxNum++;
        string memory dataToString = bytes32ToString(data);
        string memory halfDataString = LibString.substrByCharIndex(
            dataToString,
            32,
            32
        );

        return
            LibString.concat(
                LibString.concat(
                    LibString.concat(
                        LibString.concat(srcChainCode, ":"),
                        destChainCode
                    ),
                    ":"
                ),
                halfDataString
            );
    }

    /// @notice Generate cross-chain transaction object
    function createCrossTx(
        string memory srcBid,
        string memory destBid,
        string memory srcChainCode,
        string memory destChainCode,
        uint8 txType,
        bytes memory payload,
        TxOriginEnum origin,
        string memory crossTxNo
    ) internal returns (_CrossTxObj memory) {
        if (
            keccak256(abi.encodePacked(crossTxNo)) ==
            keccak256(abi.encodePacked(""))
        ) {
            crossTxNo = createCrossTxNo(srcChainCode, destChainCode);
        }
        _CrossTxObj memory crossTxTemp;

        crossTxTemp.CrossTxNo = crossTxNo;
        crossTxTemp.SrcChainCode = srcChainCode;
        crossTxTemp.DestChainCode = destChainCode;
        //源链地址
        crossTxTemp.SrcBid = srcBid;
        crossTxTemp.DestBid = destBid;
        crossTxTemp.TxType = txType;
        crossTxTemp.Payload = payload;
        crossTxTemp.Result = TxResultEnum.INIT;
        crossTxTemp.Refunded = TxRefundedEnum.NONE;
        crossTxTemp.Origin = origin;

        return crossTxTemp;
    }

    /// @notice Number of points that can be transferred by authorization
    //授权可转让积分数
    function approve(
        address from,
        address to,
        uint256 value
    ) public {
        delegateAdditional();
    }

    /// @notice Query the number of authorized points
    //查询授权点数
    function getAllowance(address owner, address spender)
        public
        view
        returns (uint256)
    {
        require(
            owner != address(0x0) && spender != address(0x0),
            "Invalid address"
        );
        uint256 value = _AllowValue[
            keccak256(abi.encodePacked(owner, spender))
        ];
        return value;
    }

    /// @notice Contract transfer main chain points to itself
    //合约转账主链指向自身    资产转账
    function transferFromContractSelf(
        uint256 asstKey,
        address from,
        address to,
        uint256 value
    ) internal {
        require(value > 0, "Amount must be greater than 0");
        uint256 fromValue = _MainAsset[
            keccak256(abi.encodePacked(asstKey, from))
        ];
        require(
            fromValue > value,
            "The main-chain points of the account are not enough"
        );

        uint256 allowValue = getAllowance(from, to);
        require(
            allowValue > value,
            "Too few points allowed for contract transfer"
        );

        uint256 toValue = _MainAsset[keccak256(abi.encodePacked(asstKey, to))];
        toValue = toValue + value;
        _MainAsset[keccak256(abi.encodePacked(asstKey, to))] = toValue;

        fromValue = fromValue - value;
        _MainAsset[keccak256(abi.encodePacked(asstKey, from))] = fromValue;

        allowValue = allowValue - value;
        _AllowValue[keccak256(abi.encodePacked(from, to))] = allowValue;
    }

    /// @notice The subchain user sends the main chain points transfer
    //子链用户发送主链积分转账
    function subStartTxGas(
        address srcAddress,
        string memory srcChainCode,
        //cross-chain transaction description
        bytes memory payload
    ) internal {
        require(
            srcAddress == msg.sender,
            "Start tx srcAddress and sender not same"
        );
        require(
            keccak256(abi.encodePacked(_ChainCode)) ==
                keccak256(abi.encodePacked(srcChainCode)),
            "Chaincode is different"
        );

        uint256 amount = abi.decode(payload, (uint256));
        transferFromContractSelf(
            _MainChainCode,
            msg.sender,
            address(this),
            amount
        );
    }

    /// @notice Subchain user sends subchain points exchange
    /// @notice Contract locked subchain points
    //子链用户发送子链积分兑换
    //合约锁定子链积分
    function subStartTxSgas(
        address srcAddress,
        string memory srcChainCode,
        bytes memory payload
    ) internal {
        require(
            srcAddress == msg.sender,
            "Start tx srcAddress and sender not same"
        );
        require(
            keccak256(abi.encodePacked(_ChainCode)) ==
                keccak256(abi.encodePacked(srcChainCode)),
            "Chaincode is different"
        );
        

        (, uint256 srcAmount, , , ) = abi.decode(
            payload,
            (uint256, uint256, string, uint256, string)
        );
        require(srcAmount == msg.value, "Amount not same");
    }

    /// @notice Subchain user sends contract interoperability
    function subStartTxCall(
        address srcAddress,
        string memory srcChainCode,
        bytes memory payload
    ) internal {
        require(
            srcAddress == msg.sender,
            "Start tx srcAddress and sender not same"
        );
        require(
            keccak256(abi.encodePacked(_ChainCode)) ==
                keccak256(abi.encodePacked(srcChainCode)),
            "Chaincode is different"
        );

        (, , bytes memory token) = abi.decode(payload, (string, bytes, bytes));

        if (token.length != 0) {
            (, uint256 srcAmount, , , ) = abi.decode(
                payload,
                (uint256, uint256, string, uint256, string)
            );
            require(srcAmount == msg.value, "Amount not same");
        }
    }

    /// @notice Users of the subchain initiate cross-chain transactions
    function startTx(
        address srcAddress,
        string memory destBid,
        string memory srcChainCode,
        string memory destChainCode,
        uint8 txType,
        bytes memory payload,
        string memory extension,
        string memory remark,
        string memory version
    ) public payable {
        require(
            keccak256(abi.encodePacked(_Version)) ==
                keccak256(abi.encodePacked(version)),
            "Version is different"
        );
        require(
            txType == 0 || txType == 1 || txType == 2,
            "Wrong transaction type"
        );

        /// @notice There are three types of cross-chain transactions
        if (txType == 0) {
            subStartTxGas(srcAddress, srcChainCode, payload);
        } else if (txType == 1) {
            subStartTxSgas(srcAddress, srcChainCode, payload);
        } else {
            subStartTxCall(srcAddress, srcChainCode, payload);
        }

        string memory srcAddr = addressToString(srcAddress);
        _CrossTxObj memory crossTxObj = createCrossTx(
            srcAddr,
            destBid,
            srcChainCode,
            destChainCode,
            txType,
            payload,
            TxOriginEnum.SRC,
            ""
        );
        crossTxObj.Extension = extension;
        crossTxObj.Remark = remark;
        crossTxObj.Version = version;
        _CrossTxObject[
            keccak256(abi.encodePacked(crossTxObj.CrossTxNo))
        ] = crossTxObj;

        emit startTxEvent("startTx", crossTxObj.CrossTxNo, txType);
    }

    /// @notice Check whether it is a gateway node
    //检查是否为网关节点
    function checkGateNode() internal view returns (bool) {
        for (uint256 i = 0; i < _GatewayList.length; i++) {
            if (_GatewayList[i] == msg.sender) {
                return true;
            }
        }
        return false;
    }

    /// @notice Casting main chain points
    //构造主链节点  只是增加了个数 ，没有标识具体节点的位置地址啥的
    function assetMint(address destAddress, uint256 amount) internal {
        //_mianAsset 是节点数
        uint256 toValue = _MainAsset[
            keccak256(abi.encodePacked(_MainChainCode, destAddress))
        ];
        toValue = toValue + amount;
        _MainAsset[
            keccak256(abi.encodePacked(_MainChainCode, destAddress))
        ] = toValue;

        emit AssetMintEvent("mint", address(this), destAddress, amount);
    }

    /// @notice Destroy main chain points
    function assetBurn(address destAddress, uint256 amount) internal {
        uint256 remainValue = _MainAsset[
            keccak256(abi.encodePacked(_MainChainCode, destAddress))
        ];
        remainValue = remainValue - amount;
        _MainAsset[
            keccak256(abi.encodePacked(_MainChainCode, destAddress))
        ] = remainValue;

        emit AssetBurnEvent("burn", address(this), amount);
    }

    /// @notice The subchain gateway submits the main chain points transfer transaction
    //子链网关提交主链积分转账交易
    //payload  : 交易描述
    function subSendTxGas(address destAddress, bytes memory payload) internal {
        uint256 amount = abi.decode(payload, (uint256));
        assetMint(destAddress, amount);   //????  构建节点    好像逻辑也适合转账
    }

    /// @notice The subchain gateway submits the subchain point exchange transaction
    //子链网关提交子链积分兑换交易
    function subSendTxSgas(address payable destAddress, bytes memory payload)
        internal
    {
        (, , , uint256 destAmount, ) = abi.decode(
            payload,
            (uint256, uint256, string, uint256, string)
        );
        require(destAmount == msg.value, "Amount not same");
        destAddress.transfer(destAmount);
    }

    /// @notice Subchain gateway submits contract interoperability transaction
    //子链网关提交合约互操作交易
    function subSendTxCall(address payable destAddress, bytes memory payload)
        internal
    {
        (string memory _method, bytes memory _params, bytes memory token) = abi
            .decode(payload, (string, bytes, bytes));

        (, , , uint256 destAmount, ) = abi.decode(
            token,
            (uint256, uint256, string, uint256, string)
        );
        require(destAmount == msg.value, "Amount not same");

        (bool success, ) = destAddress.call{value: destAmount}(
            abi.encodePacked(
                bytes4(keccak256(abi.encodePacked(_method))),
                _params
            )
        );

        require(success, "Contract call failed");
    }

    /// @notice Gateway node submits cross-chain transaction
    function sendTx(
        string memory srcBid,
        address payable destAddress,
        string memory srcChainCode,
        string memory destChainCode,
        uint8 txType,
        string memory crossTxNo,
        bytes memory payload,
        string memory extension,
        string memory remark,
        string memory version,
        bytes memory proof
    ) public payable {
        require(checkGateNode(), "This function is restricted to the gateway");
        require(destAddress != address(0x0), "Invalid address");
        require(
            keccak256(abi.encodePacked(_Version)) ==
                keccak256(abi.encodePacked(version)),
            "Version is different"
        );
        require(
            txType == 0 || txType == 1 || txType == 2,
            "Wrong transaction type"
        );

        string memory destAddr = addressToString(destAddress);
        _CrossTxObj memory crossTxObj = createCrossTx(
            srcBid,
            destAddr,
            srcChainCode,
            destChainCode,
            txType,
            payload,
            TxOriginEnum.DEST,
            crossTxNo
        );
        crossTxObj.Extension = extension;
        crossTxObj.Remark = remark;
        crossTxObj.Version = version;

        /// @notice There are three types of cross-chain transactions
        if (txType == 0) {
            subSendTxGas(destAddress, payload);
        } else if (txType == 1) {
            subSendTxSgas(destAddress, payload);
        } else if (txType == 2) {
            subSendTxCall(destAddress, payload);
        }

        (uint256 ledgerSeq, string memory txHash) = abi.decode(
            proof,
            (uint256, string)
        );
        _SendProof memory sendProof = _SendProof({
            LedgerSeq: ledgerSeq,
            TxHash: txHash,
            verifieraddr: msg.sender
        });
        crossTxObj.SendProofs = sendProof;
        _CrossTxObject[keccak256(abi.encodePacked(crossTxNo))] = crossTxObj;

        emit sendTxEvent("sendTx", crossTxNo, txType);
    }

    /// @notice The subchain gateway submits the main chain points transfer confirmation transaction
    //子链网关提交主链积分转账确认交易
    function subSendAckSrcGas(
        _CrossTxObj memory crossTx_Obj,
        TxResultEnum result,
        uint256 amount
    ) internal returns (_CrossTxObj memory) {
        if (result == TxResultEnum.ACK_SUCCESS) {
            assetBurn(address(this), amount);
        } else {
            crossTx_Obj.Refunded = TxRefundedEnum.TODO;
        }

        return crossTx_Obj;
    }

    /// @notice The subchain gateway submits the subchain point exchange confirmation transaction
    function subSendAckSrcSgas(
        _CrossTxObj memory crossTx_Obj,
        TxResultEnum result,
        uint256 srcAmount
    ) internal returns (_CrossTxObj memory) {
        if (result == TxResultEnum.ACK_SUCCESS) {
            address payable addr = address(uint160(_GatewayList[0]));
            addr.transfer(srcAmount);
        } else {
            crossTx_Obj.Refunded = TxRefundedEnum.TODO;
        }

        return crossTx_Obj;
    }

    /// @notice The subchain gateway submits the contract interoperability confirmation transaction
    function subSendAckSrcCall(
        _CrossTxObj memory crossTx_Obj,
        TxResultEnum result,
        uint256 srcAmount
    ) internal returns (_CrossTxObj memory) {
        if (result == TxResultEnum.ACK_SUCCESS) {
            address payable addr = address(uint160(_GatewayList[0]));
            addr.transfer(srcAmount);
        } else {
            crossTx_Obj.Refunded = TxRefundedEnum.TODO;
        }

        return crossTx_Obj;
    }

    /// @notice Gateway node submits cross-chain confirmation transaction
    function sendAcked(
        string memory crossTxNo,
        TxResultEnum result,
        string memory version,
        bytes memory proof
    ) public {
        require(checkGateNode(), "This function is restricted to the gateway");
        require(
            result == TxResultEnum.ACK_SUCCESS ||
                result == TxResultEnum.ACK_FAIL ||
                result == TxResultEnum.ACK_TIMEOUT,
            "Invalid result"
        );
        require(
            keccak256(abi.encodePacked(_Version)) ==
                keccak256(abi.encodePacked(version)),
            "Version is different"
        );

        _CrossTxObj memory crossTxObj = _CrossTxObject[
            keccak256(abi.encodePacked(crossTxNo))
        ];
        require(
            crossTxObj.Result == TxResultEnum.INIT,
            "CrossTx result is not init"
        );
        TxOriginEnum origin = crossTxObj.Origin;

        if (origin == TxOriginEnum.SRC) {
            if (crossTxObj.TxType == 0) {
                uint256 amount = abi.decode(crossTxObj.Payload, (uint256));
                crossTxObj = subSendAckSrcGas(crossTxObj, result, amount);
            } else if (crossTxObj.TxType == 1) {
                (, uint256 srcAmount, , , ) = abi.decode(
                    crossTxObj.Payload,
                    (uint256, uint256, string, uint256, string)
                );
                crossTxObj = subSendAckSrcSgas(crossTxObj, result, srcAmount);
            } else if (crossTxObj.TxType == 2) {
                uint256 _srcAmount;
                (, , bytes memory token) = abi.decode(
                    crossTxObj.Payload,
                    (string, bytes, bytes)
                );
                if (token.length != 0) {
                    (, uint256 srcAmount, , , ) = abi.decode(
                        token,
                        (uint256, uint256, string, uint256, string)
                    );
                    _srcAmount = srcAmount;
                }
                crossTxObj = subSendAckSrcCall(crossTxObj, result, _srcAmount);
            }
        }

        crossTxObj.Result = TxResultEnum(result);
        (uint256 ledgerSeq, string memory txHash) = abi.decode(
            proof,
            (uint256, string)
        );
        _AckProof memory ackProof = _AckProof({
            LedgerSeq: ledgerSeq,
            TxHash: txHash,
            verifieraddr: msg.sender
        });
        crossTxObj.AckProofs = ackProof;
        _CrossTxObject[keccak256(abi.encodePacked(crossTxNo))] = crossTxObj;

        emit sendAckedEvent(
            "sendAcked",
            crossTxObj.CrossTxNo,
            crossTxObj.TxType
        );
    }

    /// @notice Withdraw main chain points
    //提取主链积分
    function takeOutgas(
        bytes memory payload,
        address contractAddr,
        address to
    ) internal {
        uint256 amount = abi.decode(payload, (uint256));

        uint256 fromValue = _MainAsset[
            keccak256(abi.encodePacked(_MainChainCode, contractAddr))
        ];

        uint256 toValue = _MainAsset[
            keccak256(abi.encodePacked(_MainChainCode, to))
        ];
        toValue = toValue + amount;
        _MainAsset[keccak256(abi.encodePacked(_MainChainCode, to))] = toValue;

        fromValue = fromValue - amount;
        _MainAsset[
            keccak256(abi.encodePacked(_MainChainCode, contractAddr))
        ] = fromValue;
    }

    /// @notice Withdraw subchain points
    function takeOutSgas(bytes memory payload, address payable to) internal {
        (, uint256 srcAmount, , , ) = abi.decode(
            payload,
            (uint256, uint256, string, uint256, string)
        );
        to.transfer(srcAmount);
    }

    /// @notice Withdrawal of contractual interoperability assets
    function takeOutCall(bytes memory payload, address payable to) internal {
        uint256 _srcAmount;
        (, , bytes memory token) = abi.decode(payload, (string, bytes, bytes));
        if (token.length != 0) {
            (, uint256 srcAmount, , , ) = abi.decode(
                token,
                (uint256, uint256, string, uint256, string)
            );
            _srcAmount = srcAmount;
        }
        to.transfer(_srcAmount);
    }

    /// @notice Withdrawal of abnormal cross-chain assets
    function takeOut(string memory crossTxNo, address payable to) public {
        _CrossTxObj memory crossTxObj = _CrossTxObject[
            keccak256(abi.encodePacked(crossTxNo))
        ];
        require(crossTxObj.Origin == TxOriginEnum.SRC, "Not src");
        require(
            crossTxObj.Result == TxResultEnum.ACK_TIMEOUT ||
                crossTxObj.Result == TxResultEnum.ACK_FAIL,
            "Result is not timeout or fail"
        );
        string memory str = addressToString(msg.sender);
        require(
            keccak256(abi.encodePacked(str)) ==
                keccak256(abi.encodePacked(crossTxObj.SrcBid)),
            "Not your asset"
        );
        require(
            crossTxObj.Refunded == TxRefundedEnum.TODO,
            "The asset has been returned"
        );

        if (crossTxObj.TxType == 0) {
            takeOutgas(crossTxObj.Payload, address(this), to);
        } else if (crossTxObj.TxType == 1) {
            takeOutSgas(crossTxObj.Payload, to);
        } else if (crossTxObj.TxType == 2) {
            takeOutCall(crossTxObj.Payload, to);
        }

        crossTxObj.Refunded = TxRefundedEnum.REFUNDED;
        _CrossTxObject[keccak256(abi.encodePacked(crossTxNo))] = crossTxObj;

        emit takeOutEvent("takeOut", to);
    }

    /// @notice Transfer main chain points in subchain
    function transfer(address to, uint256 value) public {
        delegateAdditional();
    }

    /// @notice Query cross-chain transaction information
    function getCrossTx(string memory crossTxNo)
        public
        view
        returns (
            bytes memory,
            bytes memory,
            TxResultEnum,
            TxRefundedEnum,
            TxOriginEnum,
            bytes memory,
            bytes memory,
            bytes memory
        )
    {
        _CrossTxObj memory crossObj = _CrossTxObject[
            keccak256(abi.encodePacked(crossTxNo))
        ];

        bytes memory basicInfo = abi.encode(
            crossObj.SrcChainCode,
            crossObj.DestChainCode,
            crossObj.SrcBid,
            crossObj.DestBid,
            uint256(crossObj.TxType)
        );
        bytes memory extensionInfo = abi.encode(
            crossObj.Remark,
            crossObj.Extension,
            crossObj.Version
        );
        bytes memory sendProof = abi.encode(
            crossObj.SendProofs.LedgerSeq,
            crossObj.SendProofs.TxHash,
            crossObj.SendProofs.verifieraddr
        );
        bytes memory ackProof = abi.encode(
            crossObj.AckProofs.LedgerSeq,
            crossObj.AckProofs.TxHash,
            crossObj.AckProofs.verifieraddr
        );

        return (
            basicInfo,
            crossObj.Payload,
            crossObj.Result,
            crossObj.Refunded,
            crossObj.Origin,
            sendProof,
            ackProof,
            extensionInfo
        );
    }

    /// @notice Query main chain points balance
    function balanceOf(address addr) public view returns (uint256) {
        return _MainAsset[keccak256(abi.encodePacked(_MainChainCode, addr))];
    }

    /// @notice Query contract version information
    function getVersion() public view returns (string memory) {
        return _Version;
    }

    /// @notice The address type is converted to string
    function addressToString(address account)
        internal
        pure
        returns (string memory)
    {
        bytes memory temp = abi.encodePacked(account);
        bytes memory alphabet = "0123456789abcdef";

        bytes memory str = new bytes(2 + temp.length * 2);
        str[0] = "0";
        str[1] = "x";
        for (uint256 i = 0; i < temp.length; i++) {
            str[2 + i * 2] = alphabet[uint256(uint8(temp[i] >> 4))];
            str[3 + i * 2] = alphabet[uint256(uint8(temp[i] & 0x0f))];
        }
        return string(str);
    }

    /// @notice The bytes32 type is converted to string
    function bytes32ToString(bytes32 value)
        internal
        pure
        returns (string memory)
    {
        bytes memory temp = abi.encodePacked(value);
        bytes memory alphabet = "0123456789abcdef";

        bytes memory str = new bytes(2 + temp.length * 2);

        for (uint256 i = 0; i < temp.length; i++) {
            str[0 + i * 2] = alphabet[uint256(uint8(temp[i] >> 4))];
            str[1 + i * 2] = alphabet[uint256(uint8(temp[i] & 0x0f))];
        }
        return string(str);
    }

    /// @notice Contract delegatecall  委托调用？？
    function delegateAdditional() internal {
        address _target = address(_CrossChainContractAdditional);
        // assembly 内联汇编,在没有solidity的条件下也能运行
        assembly {
            // The pointer to the free memory slot     指向空闲内存槽的指针
            let ptr := mload(0x40)
            // Copy function signature and arguments from calldata at zero position into memory at pointer position
            //将函数签名和参数从零位置的调用数据复制到指针位置的内存中
            //calldatacopy(t, f, s)		从调用数据的位置 f 的拷贝 s 个字节到内存的位置 t
            //calldatasize	 		调用数据的字节数大小
            calldatacopy(ptr, 0x0, calldatasize())
            // Delegatecall method of the implementation contract, returns 0 on error
            //实现合约的Delegatecall方法，错误返回0
            //delegatecall(g, a, in, insize, out, outsize)	 		与 callcode 等价且保留 caller 和 callvalue
            //callcode(g, a, v, in, insize, out, outsize)	 		与 call 等价，但仅使用地址 a 中的代码 且保持当前合约的执行上下文
            //call(g, a, v, in, insize, out, outsize)	 		使用 mem[in...(in + insize)) 作为输入数据， 提供 g gas 和 v wei 对地址 a 发起消息调用， 输出结果数据保存在 mem[out...(out + outsize))， 发生错误（比如 gas 不足）时返回 0，正确结束返回 1
            let result := delegatecall(
                gas(),
                _target,
                ptr,
                calldatasize(),
                0x0,
                0
            )
            // Get the size of the last return data
            //获取最后返回数据的大小
            let size := returndatasize()
            // Copy the size length of bytes from return data at zero position to pointer position
            //将字节的大小长度从零位置的返回数据复制到指针位置
            returndatacopy(ptr, 0x0, size)

            // Depending on result value
            switch result
            //0发生错误
            case 0 {
                // End execution and revert state changes
                //结束执行并恢复状态更改
                revert(ptr, size)
            }
            default {
                // Return data with length of size at pointers position
                return(ptr, size)
            }
        }
    }
}

