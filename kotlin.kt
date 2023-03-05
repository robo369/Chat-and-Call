// برای شروع کردن تماس
private fun startCall() {
    val signalingParameters = createSignalingParameters()
    val videoCapturer = createVideoCapturer()

    // تنظیمات WebRTC
    val rtcConfig = createRtcConfig()
    val rtcEvents = createRtcEvents()

    // برقراری اتصال با سرور
    val peerConnectionClient = PeerConnectionClient.getInstance()
    peerConnectionClient.createPeerConnectionFactory(context)
    peerConnectionClient.createPeerConnection(signalingParameters, rtcConfig, rtcEvents)
    peerConnectionClient.createOffer()
}

// برای اتصال به سرور
private fun createSignalingParameters(): SignalingParameters {
    // پارامترهای سرور را بگیرید
    val signalingParameters = SignalingParameters(
        iceServers, true, "", "", null, null,
        null
    )
    return signalingParameters
}

// برای ایجاد ویدئو کپچر
private fun createVideoCapturer(): VideoCapturer {
    // نوع ویدئو کپچر را برای دستگاه مورد نظر انتخاب کنید
    return Camera2Enumerator(context).run {
        val deviceNames = deviceNames
        // نام دستگاهی را که قصد استفاده از آن را دارید انتخاب کنید
        val frontCameraDeviceName = deviceNames.find { name -> isFrontFacing(name) }
        createCapturer(frontCameraDeviceName, null)
    }
}

// برای ایجاد تنظیمات WebRTC
private fun createRtcConfig(): PeerConnection.RTCConfiguration {
    val rtcConfig = PeerConnection.RTCConfiguration(iceServers)
    rtcConfig.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
    rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY
    return rtcConfig
}

// برای ایجاد رویدادهای WebRTC
private fun createRtcEvents(): PeerConnectionEvents {
    return object : PeerConnectionEvents {
        override fun onLocalDescription(sdp: SessionDescription) {
            // توصیف جلسه (Session Description) ایجاد شده را برای سرور بفرستید
        }

        override fun onIceCandidate(iceCandidate: IceCandidate) {
            // iceCandidate را به سرور بفرستید
        }

        override fun onIceCandidatesRemoved(iceCandidates: Array<IceCandidate>) {
            // ice
