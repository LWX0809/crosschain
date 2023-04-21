pragma solidity >=0.4.22 <0.8.0;

pragma experimental ABIEncoderV2;

import "./L_Storage.sol";
import "./L_CrossChainContractAdditional.sol";

contract L_CrossChainContract is L_Storage {
    //合约是否被初始化
    bool private _Init;

    //跨链合约初始化，只能被执行一次
    function initialize(address crossChainAdd) public initializer{
        _Owner=msg.sender;
        _Version="V1";
        _MainChainCode=0;
        //?????
        _CrossChainContractAdditional=L_CrossChainContractAdditional(
              crossChainAdd
        );
        _Init=true;
    }

    //修改器
    modifier onlyOwner(){
        require(
              msg.sender==_Owner,
              "z这个方法显示合约的owener"
         );
        _;
    }

    modifier initializer(){
        require(_Init==false,"The contract has been initialized");
        _;
    }

    //设置子链AC码 只能由合约所有者执行   ？？？？参数有什么用
    function setChainCode(string memory chaincode) public onlyOwner{
        delegateAdditional();
    }

    //设置子链跨链网关  只能由合约所有者执行
    function setGateway(address gatewayAddress) public onlyOwner{
        delegateAdditional();
    }

    //生成跨链交易number
    // srcChainCode + ':' + destChainCode + ':' + keccak256(msg.sender, _CrossTxNum, "1").substr(0, 32).
    function createCrossTxNo(
        string memory srcChainCode,
        string memory destChainCode)
    internal returns(string memory){
        bytes32 data=keccak256(
        //abi.encodePacked 在没有填充的情况下进行编码
             abi.encodePacked(msg.sender,_CrossTxNum,"1")
        );
        _CrossTxNum++;
        string memory dataToString =bytes32ToString(data);
        string memory halfDataString = LibString.substrByCharIndex(
            dataToString,
            32,
            32
        );

        return  LibString.concat(
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

    //生成跨链交易对象
    //srcBid:源链地址   destBid：目标链地址  srcChainCode：源链AC码
    //txType：交易类型 type:0,1,2,3
    //payload:跨链交易描述
    //TxOriginEnum : contract location
    function createCrossTx(
        string memory srcBid,
        string memory destBid,
        string memory srcChainCode,
        string memory destChainCode,
        uint8 txType,
        bytes memory payload,
        TxOriginEnum origin,
        string memory crossTxNo
    )internal returns (_CrossTxObj memory){
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

    //资产转账  合约转账主链指向自己
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

    ///  The subchain user sends the main chain points transfer
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
    //子链用户发送合约互操作性
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
    //子链用户发起跨链交易
    //srcAddress ，destBid：目的链地址   extension：用户评论，remark：tag information
    //version：send chain contract version information   合约版本信息
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
        //存储crossTxObj进mapping里
        _CrossTxObject[
        keccak256(abi.encodePacked(crossTxObj.CrossTxNo))
        ] = crossTxObj;

        emit startTxEvent("startTx", crossTxObj.CrossTxNo, txType);
    }

    //检查是否为网关节点
    function checkGateNode() internal view returns (bool) {
        for (uint256 i = 0; i < _GatewayList.length; i++) {
            if (_GatewayList[i] == msg.sender) {
                return true;
            }
        }
        return false;
    }

    //构造destAddress主链节点
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

    //销毁主链节点
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

    //网关节点提交跨链交易
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
        ///  block number where the transaction is located
        ///  hash of transaction
        ///  verifier of startTx transactions
        _SendProof memory sendProof = _SendProof({
        LedgerSeq: ledgerSeq,
        TxHash: txHash,
        verifieraddr: msg.sender
        });
        crossTxObj.SendProofs = sendProof;
        _CrossTxObject[keccak256(abi.encodePacked(crossTxNo))] = crossTxObj;

        emit sendTxEvent("sendTx", crossTxNo, txType);
    }

    //子链网关提交主链积分转账确认交易
    function subSendAckSrcGas(
        _CrossTxObj memory crossTx_Obj,
        TxResultEnum result,
        uint256 amount
    ) internal returns (_CrossTxObj memory) {
        if (result == TxResultEnum.ACK_SUCCESS) {
            //销毁主链节点
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
    //与上面方法一样
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
    //网关节点提交跨链确认交易
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

        //source chain
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













}
