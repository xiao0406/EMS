package com.cecec.api.base;

public class MeterPayloadEntity {
    /**
     * 正向有功总电能
     */
    private Double ep;
    /**
     *反向有功总电能
     */
    private Double erp;
    /**
     *正向无功总电能
     */
    private Double eqi;
    /**
     *反向无功总电能
     */
    private Double erqi;
    /**
     *A相电压
     */
    private Double ua;
    /**
     *B相电压
     */
    private Double ub;
    /**
     *C相电压
     */
    private Double uc;
    /**
     *A相电流
     */
    private Double ia;
    /**
     *B相电流
     */
    private Double ib;
    /**
     *C相电流
     */
    private Double ic;
    /**
     *总有功功率
     */
    private Double p;
    /**
     *A相有功功率
     */
    private Double pa;
    /**
     *B相有功功率
     */
    private Double pb;
    /**
     *C相有功功率
     */
    private Double pc;
    /**
     *总无功功率
     */
    private Double q;
    /**
     *A相无功功率
     */
    private Double qa;
    /**
     *B相无功功率
     */
    private Double qb;
    /**
     *C相无功功率
     */
    private Double qc;
    /**
     *总功率因数
     */
    private Double pf;
    /**
     *A相功率因数
     */
    private Double pfa;
    /**
     *B相功率因数
     */
    private Double pfb;
    /**
     *C相功率因数
     */
    private Double pfc;

    public Double getEp() {
        return ep;
    }

    public void setEp(Double ep) {
        this.ep = ep;
    }

    public Double getErp() {
        return erp;
    }

    public void setErp(Double erp) {
        this.erp = erp;
    }

    public Double getEqi() {
        return eqi;
    }

    public void setEqi(Double eqi) {
        this.eqi = eqi;
    }

    public Double getErqi() {
        return erqi;
    }

    public void setErqi(Double erqi) {
        this.erqi = erqi;
    }

    public Double getUa() {
        return ua;
    }

    public void setUa(Double ua) {
        this.ua = ua;
    }

    public Double getUb() {
        return ub;
    }

    public void setUb(Double ub) {
        this.ub = ub;
    }

    public Double getUc() {
        return uc;
    }

    public void setUc(Double uc) {
        this.uc = uc;
    }

    public Double getIa() {
        return ia;
    }

    public void setIa(Double ia) {
        this.ia = ia;
    }

    public Double getIb() {
        return ib;
    }

    public void setIb(Double ib) {
        this.ib = ib;
    }

    public Double getIc() {
        return ic;
    }

    public void setIc(Double ic) {
        this.ic = ic;
    }

    public Double getP() {
        return p;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public Double getPa() {
        return pa;
    }

    public void setPa(Double pa) {
        this.pa = pa;
    }

    public Double getPb() {
        return pb;
    }

    public void setPb(Double pb) {
        this.pb = pb;
    }

    public Double getPc() {
        return pc;
    }

    public void setPc(Double pc) {
        this.pc = pc;
    }

    public Double getQ() {
        return q;
    }

    public void setQ(Double q) {
        this.q = q;
    }

    public Double getQa() {
        return qa;
    }

    public void setQa(Double qa) {
        this.qa = qa;
    }

    public Double getQb() {
        return qb;
    }

    public void setQb(Double qb) {
        this.qb = qb;
    }

    public Double getQc() {
        return qc;
    }

    public void setQc(Double qc) {
        this.qc = qc;
    }

    public Double getPf() {
        return pf;
    }

    public void setPf(Double pf) {
        this.pf = pf;
    }

    public Double getPfa() {
        return pfa;
    }

    public void setPfa(Double pfa) {
        this.pfa = pfa;
    }

    public Double getPfb() {
        return pfb;
    }

    public void setPfb(Double pfb) {
        this.pfb = pfb;
    }

    public Double getPfc() {
        return pfc;
    }

    public void setPfc(Double pfc) {
        this.pfc = pfc;
    }
}
